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

import cats.data.Chain
import cats.{Applicative, Show}
import cats.effect.concurrent.Ref
import cats.syntax.show._
import cats.syntax.applicative._

class TestConsole[F[_]: Applicative](out1: Ref[F, Chain[String]],
                                     out2: Ref[F, Chain[String]],
                                     out3: Ref[F, Chain[String]])
    extends Console[F] {
  override def putStrLn[A: Show](a: A): F[Unit] =
    out1.update(acc => acc.append(a.show))
  override def putStr[A: Show](a: A): F[Unit] =
    out2.update(acc => acc.append(a.show))
  override def putError[A: Show](a: A): F[Unit] =
    out3.update(acc => acc.append(a.show))
  override val readLn: F[String] = "test".pure[F]
}
