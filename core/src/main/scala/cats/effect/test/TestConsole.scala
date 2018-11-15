/*
 * Copyright 2018 Typelevel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cats.effect.test

import cats.data.Chain
import cats.effect.concurrent.Ref
import cats.effect.{ Console, Sync }
import cats.syntax.functor._
import cats.syntax.show._
import cats.{ Applicative, Show }

class TestConsole[F[_]: Applicative](outLines: Ref[F, Chain[String]],
                                     outWords: Ref[F, Chain[String]],
                                     outErrors: Ref[F, Chain[String]],
                                     val readLn: F[String])
    extends Console[F] {

  override def putStrLn[A: Show](a: A): F[Unit] =
    outLines.update(_.append(a.show))

  override def putStr[A: Show](a: A): F[Unit] =
    outWords.update(_.append(a.show))

  override def putError[A: Show](a: A): F[Unit] =
    outErrors.update(_.append(a.show))
}

object TestConsole {

  /**
    * Creates a console that, instead of using standard input/output,
    * appends printed lines to Refs and calls the `readLn` action to get stubbed input.
    *
    * Meant for testing purposes, not for production usage.
    * */
  def make[F[_]: Applicative](outLines: Ref[F, Chain[String]],
                              outWords: Ref[F, Chain[String]],
                              outErrors: Ref[F, Chain[String]],
                              readLn: F[String]): Console[F] =
    new TestConsole[F](outLines, outWords, outErrors, readLn)

  object inputs {

    /**
      * The outer F will produce an action that (when called) will return
      * successive elements of the `inputs` sequence.
      *
      * When it runs out of elements, it'll help returning `default`.
      *
      * @example {{{
      * for {
      *   inputsF <- TestConsole.inputs.sequenceAndDefault(Chain.of("foo", "bar"), "default")
      *   console =  TestConsole.make(ref, ref, ref, inputsF)
      *   lines   <- console.readLn.replicateA(4)
      * } yield (lines == Chain.of("foo", "bar", "default", "default"))
      * }}}
      * */
    def sequenceAndDefault[F[_]: Sync](inputs: Chain[String], default: String): F[F[String]] =
      Ref[F].of(inputs).map {
        _.modify {
          _.uncons match {
            case Some((head, tail)) => (tail, head)
            case None               => (Chain.nil, default)
          }
        }
      }
  }
}
