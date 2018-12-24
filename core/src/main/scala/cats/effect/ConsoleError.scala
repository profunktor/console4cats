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

import cats.effect.transformed.TransformedConsoleError
import cats.instances.string._
import cats.{ ~>, Show }

trait ConsoleError[F[_]] { self =>

  /**
    * Prints a message of type A followed by a new line to the error output using the implicit `Show[A]` instance.
    */
  def putError[A: Show](a: A): F[Unit]

  /**
    * Prints a message to the error output.
    */
  def putError(str: String): F[Unit] = putError[String](str)

  /**
    * Transforms this console using a FunctionK.
    * */
  def mapK[G[_]](fk: F ~> G): ConsoleError[G] =
    new TransformedConsoleError[F, G] {
      override protected val underlying: ConsoleError[F] = self
      override protected val f: F ~> G                   = fk
    }
}
object ConsoleError {
  def apply[F[_]](implicit F: ConsoleError[F]): ConsoleError[F] = F
}
