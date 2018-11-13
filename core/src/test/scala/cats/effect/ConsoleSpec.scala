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

import cats.data.EitherT
import cats.effect.concurrent.Ref
import cats.instances.boolean._
import cats.instances.double._
import cats.instances.int._
import cats.syntax.all._
import cats.{Applicative, Monad, Show}
import org.scalatest.FunSuite

class ConsoleSpec extends FunSuite {

  case class TestConsole[F[_]: Applicative](
      out1: Ref[F, List[String]],
      out2: Ref[F, List[String]],
      out3: Ref[F, List[String]]
  ) extends Console[F] {
    override def putStrLn[A: Show](a: A): F[Unit] =
      out1.update(acc => acc ::: a.show :: Nil)
    override def putStr[A: Show](a: A): F[Unit] =
      out2.update(acc => acc ::: a.show :: Nil)
    override def putError[A: Show](a: A): F[Unit] =
      out3.update(acc => acc ::: a.show :: Nil)
    override def readLn: F[String] = Applicative[F].pure("test")
  }

  private def program[F[_]: Monad](implicit C: Console[F]): F[String] =
    for {
      _ <- C.putStrLn("a")
      _ <- C.putStrLn(true)
      _ <- C.putStr(123)
      _ <- C.putStr("b")
      n <- C.readLn
      _ <- C.putError(n)
      _ <- C.putError(1.5)
    } yield n

  test("Console") {
    val test =
      for {
        out1 <- Ref.of[IO, List[String]](List.empty[String])
        out2 <- Ref.of[IO, List[String]](List.empty[String])
        out3 <- Ref.of[IO, List[String]](List.empty[String])
        cio = TestConsole(out1, out2, out3)
        rs <- program(Monad[IO], cio)
        rs1 <- out1.get
        rs2 <- out2.get
        rs3 <- out3.get
      } yield {
        assert(rs == "test")
        assert(rs1.mkString(",") == "a,true")
        assert(rs2.mkString(",") == "123,b")
        assert(rs3.mkString(",") == "test,1.5")
      }

    test.unsafeRunSync()
  }

  test("mapK") {
    type E[A] = EitherT[IO, String, A]

    val test =
      for {
        out1 <- Ref.of[IO, List[String]](List.empty[String])
        out2 <- Ref.of[IO, List[String]](List.empty[String])
        out3 <- Ref.of[IO, List[String]](List.empty[String])
        rs <- {
          implicit val console =
            TestConsole[IO](out1, out2, out3).mapK(EitherT.liftK[IO, String])
          program[E].value
        }
        rs1 <- out1.get
        rs2 <- out2.get
        rs3 <- out3.get
      } yield {
        assert(rs.right.get == "test")
        assert(rs1.mkString(",") == "a,true")
        assert(rs2.mkString(",") == "123,b")
        assert(rs3.mkString(",") == "test,1.5")
      }

    test.value.unsafeRunSync()
  }

}
