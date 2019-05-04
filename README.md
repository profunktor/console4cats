console4cats
============

[![CircleCI](https://circleci.com/gh/gvolpe/console4cats.svg?style=svg)](https://circleci.com/gh/gvolpe/console4cats)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.gvolpe/console4cats_2.12.svg)](http://search.maven.org/#search%7Cga%7C1%7Cconsole4cats) <a href="https://typelevel.org/cats/"><img src="https://typelevel.org/cats/img/cats-badge.svg" height="40px" align="right" alt="Cats friendly" /></a>

Effect-type agnostic Console I/O for `Cats Effect`

```scala
import cats.effect.IO
import cats.effect.Console.io._

val program: IO[Unit] =
  readLn.flatMap(name => putStrLn(s"Hello $name!"))
```

### Getting started

Add the dependency to your project:

```scala
libraryDependencies += "com.github.gvolpe" %% "console4cats" % Version
```

### Usage

See the [usage guide](https://gvolpe.github.io/console4cats/guide.html).
