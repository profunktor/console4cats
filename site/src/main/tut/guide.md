---
layout: docs
title:  "Guide"
number: 1
position: 1
---

# Usage Guide

There's a default instance defined for any `F[_]` with a `Sync` instance named `SyncConsole[F]`. You can either use this one or define your own by extending `Console[F]`.

#### DSL style with IO

```tut:book:silent
import cats.effect.IO
import cats.effect.Console.io._

val program: IO[Unit] =
  for {
    _ <- putStrLn("Please enter your name: ")
    n <- readLn
    _ <- if (n.nonEmpty) putStrLn(s"Hello $n!")
         else putError("Name is empty!")
  } yield ()
```

#### Tagless final encoding:

```tut:book:reset:silent
import cats.Monad
import cats.data.StateT
import cats.effect.{ Console, IO }
import cats.implicits._

def myProgram[F[_]: Console: Monad]: F[Unit] =
  for {
    _ <- Console[F].putStrLn("Please enter your name: ")
    n <- Console[F].readLn
    _ <- if (n.nonEmpty) Console[F].putStrLn(s"Hello $n!")
         else Console[F].putError("Name is empty!")
  } yield ()

// Providing a default instance for Console[IO]
import cats.effect.Console.implicits._

def entryPoint: IO[Unit] = myProgram[IO]

// You can also use Monad Transformers 
def mt: IO[Unit] =
  myProgram[StateT[IO, String, *]].run("foo").void
```

### TestConsole

For testing, we provide a helper that makes it easier to test console output: `cats.effect.test.TestConsole`.

```tut:book:silent
import cats.data.Chain
import cats.effect.IO
import cats.effect.concurrent.Ref
import cats.effect.test.TestConsole

val test = for {
  out1 <- Ref[IO].of(Chain.empty[String])
  out2 <- Ref[IO].of(Chain.empty[String])
  out3 <- Ref[IO].of(Chain.empty[String])
  in1 <- TestConsole.inputs
          .sequenceAndDefault[IO](Chain("foo", "bar"), "baz")

  console = TestConsole.make(out1, out2, out3, in1)

  input <- console.readLn
  _     <- console.putStrLn(input)
  _     <- console.putStr("boom")
  _     <- console.putError("err")
  rs1 <- out1.get
  rs2 <- out2.get
  rs3 <- out3.get
} yield {
  assert(rs1 == Chain.one("foo"))
  assert(rs2 == Chain.one("boom"))
  assert(rs3 == Chain.one("err"))
}
  ```
