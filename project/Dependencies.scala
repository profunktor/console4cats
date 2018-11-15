import sbt._

object Dependencies {

  object Versions {
    val catsEffect = "1.0.0"
    val scalaTest  = "3.0.5"
  }

  object Libraries {
    lazy val catsEffect = "org.typelevel" %% "cats-effect" % Versions.catsEffect
    lazy val scalaTest  = "org.scalatest" %% "scalatest"   % Versions.scalaTest % Test
  }

}
