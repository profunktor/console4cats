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

package cats.effect.transformed

import cats.effect.ConsoleIn
import cats.~>

private[effect] trait TransformedConsoleIn[F[_], G[_]] extends ConsoleIn[G] {
  protected def underlying: ConsoleIn[F]
  protected def f: F ~> G

  override def readLn: G[String] = f(underlying.readLn)
}
