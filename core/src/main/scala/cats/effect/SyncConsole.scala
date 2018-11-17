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

import java.io.{ BufferedReader, PrintStream }

import cats.Show
import cats.syntax.show._

/**
  * A default instance for any `F[_]` with a `Sync` instance.
  *
  * Construct one e.g. by calling `SyncConsole.stdIn[F]`.
  */
private class SyncConsole[F[_]](
    private val out: PrintStream,
    private val err: PrintStream,
    private val in: BufferedReader
)(implicit F: Sync[F])
    extends Console[F] {

  /**
    * Prints a message of type A followed by a new line to the output stream using the implicit `Show[A]` instance.
    * */
  def putStrLn[A: Show](a: A): F[Unit] =
    F.delay(out.println(a.show))

  /**
    * Prints a message of type A to the output stream using the implicit `Show[A]` instance.
    * */
  def putStr[A: Show](a: A): F[Unit] =
    F.delay(out.print(a.show))

  /**
    * Prints a message of type A followed by a new line to the error output stream using the implicit `Show[A]` instance.
    * */
  def putError[A: Show](a: A): F[Unit] =
    F.delay(err.println(a.show))

  /**
    * Reads a line from the console input stream.
    *
    * @return a value representing the user's input.
    * */
  def readLn: F[String] =
    F.delay(in.readLine())

  // Overrides needed to avoid "ambiguous reference to overloaded definition" error
  override final def putStrLn(str: String): F[Unit] = super.putStrLn(str)
  override final def putStr(str: String): F[Unit]   = super.putStr(str)
  override final def putError(str: String): F[Unit] = super.putError(str)
}

object SyncConsole {

  /**
    * Constructs a console that prints to standard output, prints errors to standard error output, and reads from standard input.
    *
    * For a variant that allows customization of input/output streams, see [[make]].
    * */
  def stdio[F[_]: Sync]: Console[F] =
    make(scala.Console.out, scala.Console.err, scala.Console.in)

  /**
    * Constructs a console that prints to the given `out` and `err` streams, and reads from the `in` reader.
    *
    * For a variant that defaults to standard input/output, see [[stdio]].
    * */
  def make[F[_]: Sync](out: PrintStream, err: PrintStream, in: BufferedReader): Console[F] =
    new SyncConsole[F](out, err, in)
}
