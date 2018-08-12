package cats.effect.console

import cats.Show
import cats.effect.{IO, Sync}
import cats.instances.string._
import cats.syntax.show._

/**
  * Effect type agnostic `Console` with common methods to write and read from the standard console.
  */
trait Console[F[_]] {

  /**
    * Prints a message of type A to the standard console followed by a new line if an instance `Show[A]` is found.
    */
  def putStrLn[A: Show](a: A): F[Unit]

  /**
    * Prints a message to the standard console followed by a new line.
    */
  final def putStrLn(str: String): F[Unit] = putStrLn[String](str)

  /**
    * Prints a message of type A to the standard console if an instance `Show[A]` is found.
    */
  def putStr[A: Show](a: A): F[Unit]

  /**
    * Prints a message to the standard console.
    */
  final def putStr(str: String): F[Unit] = putStr[String](str)

  /**
    * Prints a message of type A to the standard error output stream if an instance `Show[A]` is found.
    */
  def putError[A: Show](a: A): F[Unit]

  /**
    * Prints a message to the standard error output stream.
    */
  final def putError(str: String): F[Unit] = putError[String](str)

  /**
    * Reads line from the standard console.
    *
    * @return a value representing the user's input or raise an error in the F context.
    */
  def readLn: F[String]
}
object Console {

  /**
    * Default instance for `Console[IO]`
    */
  object io extends SyncConsole[IO]
}

/**
  * A default instance for any `F[_]` with a `Sync` instance.
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
  *     }
  * }}}
  *
  * Or in tagless final encoding:
  *
  * {{{
  *   import cats.effect._
  *
  *   def myProgram[F[_]](implicit C: Console[F]): F[Unit] =
  *     for {
  *       _ <- C.putStrLn("Please enter your name: ")
  *       n <- C.readLn
  *       _ <- if (n.nonEmpty) C.putStrLn(s"Hello $$n!")
  *            else C.putError("Name is empty!")
  *     }
  * }}}
  *
  */
class SyncConsole[F[_]](implicit F: Sync[F]) extends Console[F] {
  def putStrLn[A: Show](a: A): F[Unit] =
    F.delay(println(a.show))
  def putStr[A: Show](a: A): F[Unit] =
    F.delay(print(a.show))
  def putError[A: Show](a: A): F[Unit] =
    F.delay(System.err.println(a.show))
  def readLn: F[String] =
    F.delay(scala.io.StdIn.readLine)
}