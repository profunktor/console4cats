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

import cats.effect.transformed.TransformedConsoleOut
import cats.instances.string._
import cats.{ ~>, Show }

trait ConsoleOut[F[_]] { self =>

  /**
    * Prints a message of type A followed by a new line to the console using the implicit `Show[A]` instance.
    */
  def putStrLn[A: Show](a: A): F[Unit]

  /**
    * Prints a message to the console followed by a new line.
    */
  def putStrLn(str: String): F[Unit] = putStrLn[String](str)

  /**
    * Prints a message of type A to the console using the implicit `Show[A]` instance.
    */
  def putStr[A: Show](a: A): F[Unit]

  /**
    * Prints a message to the console.
    */
  def putStr(str: String): F[Unit] = putStr[String](str)

  /**
    * Transforms this console using a FunctionK.
    * */
  def mapK[G[_]](fk: F ~> G): ConsoleOut[G] =
    new TransformedConsoleOut[F, G] {
      override protected val underlying: ConsoleOut[F] = self
      override protected val f: F ~> G                 = fk
    }
}
object ConsoleOut {
  def apply[F[_]](implicit F: ConsoleOut[F]): ConsoleOut[F] = F
}
