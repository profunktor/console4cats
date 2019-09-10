import sbt._

object Dependencies {

  object Versions {
    val catsEffect = "2.0.0"
    val scalaTest  = "3.0.8"
    val bm4        = "0.3.0"
  }

  object Libraries {
    val catsEffect = "org.typelevel" %% "cats-effect" % Versions.catsEffect
    val scalaTest  = "org.scalatest" %% "scalatest"   % Versions.scalaTest % Test
  }

  object CompilerPlugins {
    val bm4 = compilerPlugin("com.olegpy" %% "better-monadic-for" % Versions.bm4)
  }

}
