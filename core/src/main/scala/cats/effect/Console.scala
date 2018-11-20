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

package cats.effect

import cats.effect.transformed.TransformedConsole
import cats.~>

/**
  * Effect type agnostic `Console` with common methods to write and read from the standard console.
  *
  * For an implementation that uses standard input/output,
  * see [[Console.io]] (for `Console[IO]`) or [[SyncConsole.stdio]] (for any `F[_]: Sync`).
  *
  * Use it either in a DSL style:
  *
  * {{{
  *   import cats.effect.IO
  *   import cats.effect.Console.io._
  *
  *   val program: IO[Unit] =
  *     for {
  *       _ <- putStrLn("Please enter your name: ")
  *       n <- readLn
  *       _ <- if (n.nonEmpty) putStrLn(s"Hello $$n!")
  *            else putError("Name is empty!")
  *     } yield ()
  * }}}
  *
  * Or in Tagless Final encoding:
  *
  * {{{
  *   import cats.Monad
  *   import cats.effect._
  *
  *   def myProgram[F[_]: Monad](implicit C: Console[F]): F[Unit] =
  *     for {
  *       _ <- C.putStrLn("Please enter your name: ")
  *       n <- C.readLn
  *       _ <- if (n.nonEmpty) C.putStrLn(s"Hello $$n!")
  *            else C.putError("Name is empty!")
  *     } yield ()
  * }}}
  */
trait Console[F[_]] extends ConsoleIn[F] with ConsoleOut[F] with ConsoleError[F] { self =>

  /**
    * Transforms this console using a FunctionK.
    * */
  override def mapK[G[_]](fk: F ~> G): Console[G] = new TransformedConsole[F, G] {
    override protected val underlying: Console[F] = self
    override protected val f: ~>[F, G]            = fk
  }
}

object Console {
  def apply[F[_]](implicit F: Console[F]): Console[F] = F

  /**
    * Default instance for `Console[IO]` that prints to standard input/output streams.
    */
  val io: Console[IO] = SyncConsole.stdio[IO]
}
