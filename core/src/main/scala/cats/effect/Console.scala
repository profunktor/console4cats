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

import cats.instances.string._
import cats.{ ~>, Show }

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
trait Console[F[_]] {

  /**
    * Prints a message of type A to the console followed by a new line using the implicit `Show[A]` instance.
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
    * Prints a message of type A to the error output using the implicit `Show[A]` instance.
    */
  def putError[A: Show](a: A): F[Unit]

  /**
    * Prints a message to the error output.
    */
  def putError(str: String): F[Unit] = putError[String](str)

  /**
    * Reads a line from the console input.
    *
    * @return a value representing the user's input.
    */
  def readLn: F[String]

  /**
    * Transforms this console using a FunctionK.
    * */
  def mapK[G[_]](f: F ~> G): Console[G] = new TransformedConsole(this, f)
}

object Console {

  def apply[F[_]](implicit F: Console[F]): Console[F] = F

  /**
    * Default instance for `Console[IO]` that prints to standard input/output streams.
    */
  val io: Console[IO] = SyncConsole.stdio[IO]
}
