console4cats
============

[![CircleCI](https://circleci.com/gh/profunktor/console4cats.svg?style=svg)](https://circleci.com/gh/profunktor/console4cats)
[![Maven Central](https://img.shields.io/maven-central/v/dev.profunktor/console4cats_2.12.svg)](http://search.maven.org/#search%7Cga%7C1%7Cconsole4cats) <a href="https://typelevel.org/cats/"><img src="https://typelevel.org/cats/img/cats-badge.svg" height="40px" align="right" alt="Cats friendly" /></a>
[![MergifyStatus](https://img.shields.io/endpoint.svg?url=https://gh.mergify.io/badges/profunktor/console4cats&style=flat)](https://mergify.io)
[![Scala Steward badge](https://img.shields.io/badge/Scala_Steward-helping-brightgreen.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://scala-steward.org)

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
libraryDependencies += "dev.profunktor" %% "console4cats" % Version
```

Note: previous versions `<= 0.6.0` were published using the `com.github.gvolpe` group id.

### Code of Conduct

See the [Code of Conduct](https://console4cats.profunktor.dev/CODE_OF_CONDUCT)

### Usage

See the [usage guide](https://console4cats.profunktor.dev/guide.html).

## LICENSE

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this project except in compliance with
the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0.

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
language governing permissions and limitations under the License.
