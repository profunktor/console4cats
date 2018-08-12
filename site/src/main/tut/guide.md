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
    _ <- if (n.nonEmpty) putStrLn(s"Hello $$n!")
         else putError("Name is empty!")
  } yield ()
```

#### Tagless final encoding:

```scala
import cats.Monad
import cats.effect.Console

def myProgram[F[_]: Monad](implicit C: Console[F]): F[Unit] =
  for {
    _ <- C.putStrLn("Please enter your name: ")
    n <- C.readLn
    _ <- if (n.nonEmpty) C.putStrLn(s"Hello $$n!")
         else C.putError("Name is empty!")
  } yield ()
```
