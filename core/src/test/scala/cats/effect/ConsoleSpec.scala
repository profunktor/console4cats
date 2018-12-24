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

import cats.data.{ Chain, EitherT }
import cats.effect.concurrent.Ref
import cats.instances.boolean._
import cats.instances.double._
import cats.instances.int._
import cats.instances.string._
import cats.syntax.all._
import cats.FlatMap
import cats.effect.test.TestConsole
import org.scalatest.FunSuite

class ConsoleSpec extends FunSuite {

  def program[F[_]: FlatMap](implicit C: Console[F]): F[List[String]] =
    for {
      _ <- C.putStrLn("a")
      _ <- C.putStrLn(true)
      _ <- C.putStr(123)
      _ <- C.putStr("b")
      rd1 <- C.readLn
      _ <- C.putError(rd1)
      _ <- C.putError(1.5)
      rd2 <- C.readLn
      rd3 <- C.readLn
    } yield List(rd1, rd2, rd3)

  test("Console") {
    val test =
      for {
        out1 <- Ref[IO].of(Chain.empty[String])
        out2 <- Ref[IO].of(Chain.empty[String])
        out3 <- Ref[IO].of(Chain.empty[String])
        in1 <- TestConsole.inputs
                .sequenceAndDefault[IO](Chain("foo", "bar", "baz"), "")

        implicit0(console: Console[IO]) = TestConsole.make(out1, out2, out3, in1)

        rs <- program[IO]
        rs1 <- out1.get
        rs2 <- out2.get
        rs3 <- out3.get
      } yield {
        assert(rs == List("foo", "bar", "baz"))
        assert(rs1.mkString_("", ",", "") == "a,true")
        assert(rs2.mkString_("", ",", "") == "123,b")
        assert(rs3.mkString_("", ",", "") == "foo,1.5")
      }

    test.unsafeRunSync()
  }

  object implicit0 {
    def unapply[A](a: A): Some[A] = Some(a)
  }

  test("mapK") {
    type E[A] = EitherT[IO, String, A]

    val test =
      for {
        out1 <- Ref[IO].of(Chain.empty[String])
        out2 <- Ref[IO].of(Chain.empty[String])
        out3 <- Ref[IO].of(Chain.empty[String])
        in1 <- TestConsole.inputs
                .sequenceAndDefault[IO](Chain("foo"), "undefined")

        implicit0(console: Console[E]) = TestConsole.make(out1, out2, out3, in1).mapK(EitherT.liftK[IO, String])

        rs <- program[E].value
        rs1 <- out1.get
        rs2 <- out2.get
        rs3 <- out3.get
      } yield {
        assert(rs.right.get == List("foo", "undefined", "undefined"))
        assert(rs1.mkString_("", ",", "") == "a,true")
        assert(rs2.mkString_("", ",", "") == "123,b")
        assert(rs3.mkString_("", ",", "") == "foo,1.5")
      }

    test.unsafeRunSync()
  }

}
