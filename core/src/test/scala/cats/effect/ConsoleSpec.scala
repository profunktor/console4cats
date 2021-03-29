/*
 * Copyright 2018-2020 ProfunKtor
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

import cats._
import cats.data._
import cats.effect.test.TestConsole
import cats.implicits._
import munit.FunSuite
import cats.effect.Ref

class ConsoleSpec extends FunSuite {

  def program[F[_]: Console: FlatMap]: F[List[String]] =
    for {
      _ <- F.putStrLn("a")
      _ <- F.putStrLn(true)
      _ <- F.putStr(123)
      _ <- F.putStr("b")
      rd1 <- F.readLn
      _ <- F.putError(rd1)
      _ <- F.putError(1.5)
      rd2 <- F.readLn
      rd3 <- F.readLn
    } yield List(rd1, rd2, rd3)

  override def munitValueTransforms =
    super.munitValueTransforms :+ new ValueTransform("IO", {
      case ioa: IO[_] => ioa.unsafeToFuture
    })

  test("Console") {
    for {
      out1 <- Ref[IO].of(Chain.empty[String])
      out2 <- Ref[IO].of(Chain.empty[String])
      out3 <- Ref[IO].of(Chain.empty[String])
      in1 <- TestConsole.inputs
              .sequenceAndDefault[IO](Chain("foo", "bar", "baz"), "")
      implicit0(console: Console[IO]) <- TestConsole.make(out1, out2, out3, in1)
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
  }

  test("mapK") {
    type E[A] = EitherT[IO, String, A]

    for {
      out1 <- Ref[IO].of(Chain.empty[String])
      out2 <- Ref[IO].of(Chain.empty[String])
      out3 <- Ref[IO].of(Chain.empty[String])
      in1 <- TestConsole.inputs
              .sequenceAndDefault[IO](Chain("foo"), "undefined")
      implicit0(console: Console[E]) <- TestConsole
                                         .make(out1, out2, out3, in1)
                                         .map(_.mapK(EitherT.liftK[IO, String]))
      rs <- program[E].value
      rs1 <- out1.get
      rs2 <- out2.get
      rs3 <- out3.get
    } yield {
      assert(rs.getOrElse(fail("Either.Left")) == List("foo", "undefined", "undefined"))
      assert(rs1.mkString_("", ",", "") == "a,true")
      assert(rs2.mkString_("", ",", "") == "123,b")
      assert(rs3.mkString_("", ",", "") == "foo,1.5")
    }
  }

  // Monad Transformer instances

  def instances[F[_]: Applicative: Console] = {
    Console[OptionT[F, *]]
    Console[EitherT[F, String, *]]
    Console[IorT[F, String, *]]
    Console[Kleisli[F, String, *]]
    Console[ReaderWriterStateT[F, String, Int, Boolean, *]]
    Console[StateT[F, Int, *]]
    Console[WriterT[F, String, *]]
  }

}
