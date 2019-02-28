import sbt._

object Dependencies {

  object Versions {
    val catsEffect = "1.2.0"
    val scalaTest  = "3.0.6"
    val bm4        = "0.3.0-M4"
  }

  object Libraries {
    lazy val catsEffect = "org.typelevel" %% "cats-effect" % Versions.catsEffect
    lazy val scalaTest  = "org.scalatest" %% "scalatest"   % Versions.scalaTest % Test
  }

  object CompilerPlugins {
    val bm4 = compilerPlugin("com.olegpy" %% "better-monadic-for" % Versions.bm4)
  }

}
