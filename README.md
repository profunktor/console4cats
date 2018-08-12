console4cats
============

[![Build Status](https://travis-ci.org/gvolpe/console4cats.svg?branch=master)](https://travis-ci.org/gvolpe/console4cats)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.gvolpe/console4cats.svg)](http://search.maven.org/#search%7Cga%7C1%7Cconsole4cats)

Console I/O for `Cats Effect`.

### Getting started

Add the dependency to your project:

```
libraryDependencies += "com.github.gvolpe" %% "console4cats" % Version
```

### Usage

There's a default instance defined for any `F[_]` with a `Sync` instance named `SyncConsole[F]`. You can either use this one or define your own by extending `Console[F]`.

#### DSL style with IO

```scala
import cats.effect.IO
import cats.effect.Console.io._

val program: IO[Unit] =
  for {
    _ <- putStrLn("Please enter your name: ")
    n <- readLn
    _ <- if (n.nonEmpty) putStrLn(s"Hello $$n!")
         else putError("Name is empty!")
  }
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
