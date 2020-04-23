import sbt._

object Dependencies {

  object V {
    val catsEffect     = "2.1.3"
    val munit          = "0.7.3"
    val bm4            = "0.3.1"
    val contextApplied = "0.1.4"
    val kindProjector  = "0.11.0"
  }

  object Deps {
    val catsEffect = "org.typelevel" %% "cats-effect" % V.catsEffect
    val munit      = "org.scalameta" %% "munit"       % V.munit % Test
  }

  object CompilerPlugins {
    val bm4            = compilerPlugin("com.olegpy"     %% "better-monadic-for" % V.bm4)
    val contextApplied = compilerPlugin("org.augustjune" %% "context-applied"    % V.contextApplied)
    val kindProjector  = compilerPlugin("org.typelevel"  %% "kind-projector"     % V.kindProjector cross CrossVersion.full)
  }

}
