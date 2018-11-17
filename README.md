console4cats
============

[![Build Status](https://travis-ci.org/gvolpe/console4cats.svg?branch=master)](https://travis-ci.org/gvolpe/console4cats)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.gvolpe/console4cats_2.12.svg)](http://search.maven.org/#search%7Cga%7C1%7Cconsole4cats)

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
