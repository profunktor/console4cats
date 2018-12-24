/*
 * Copyright 2018-2019 Console for Cats
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
import cats.effect.transformed.TransformedConsoleIn
import cats.~>

trait ConsoleIn[F[_]] { self =>

  /**
    * Reads a line from the console input.
    *
    * @return a value representing the user's input.
    */
  def readLn: F[String]

  /**
    * Transforms this console using a FunctionK.
    * */
  def mapK[G[_]](fk: F ~> G): ConsoleIn[G] =
    new TransformedConsoleIn[F, G] {
      override protected val underlying: ConsoleIn[F] = self
      override protected val f: F ~> G                = fk
    }
}
object ConsoleIn {
  def apply[F[_]](implicit F: ConsoleIn[F]): ConsoleIn[F] = F
}
