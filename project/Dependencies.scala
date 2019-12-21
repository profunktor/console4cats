import sbt._

object Dependencies {

  object Versions {
    val catsEffect    = "2.0.0"
    val scalaTest     = "3.1.0"
    val bm4           = "0.3.0"
    val kindProjector = "0.10.3"
  }

  object Libraries {
    val catsEffect = "org.typelevel" %% "cats-effect" % Versions.catsEffect
    val scalaTest  = "org.scalatest" %% "scalatest"   % Versions.scalaTest % Test
  }

  object CompilerPlugins {
    val bm4           = compilerPlugin("com.olegpy"    %% "better-monadic-for" % Versions.bm4)
    val kindProjector = compilerPlugin("org.typelevel" %% "kind-projector"     % Versions.kindProjector)
  }

}
