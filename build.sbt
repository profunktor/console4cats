import com.scalapenos.sbt.prompt.SbtPrompt.autoImport._
import com.scalapenos.sbt.prompt._
import Dependencies._
import microsites.ExtraMdFileConfig

name := """console4cats"""

organization in ThisBuild := "com.github.gvolpe"

version in ThisBuild := "0.1.0-SNAPSHOT"

crossScalaVersions in ThisBuild := Seq("2.12.6")

sonatypeProfileName := "com.github.gvolpe"

promptTheme := PromptTheme(List(
  text("[SBT] ", fg(136)),
  text(_ => "console4cats", fg(64)).padRight(" λ ")
 ))

lazy val commonScalacOptions = Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:experimental.macros",
  "-unchecked",
  "-Ypartial-unification",
  "-Xfatal-warnings",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-value-discard",
  "-Xfuture",
  "-Xlog-reflective-calls",
  "-Ywarn-inaccessible",
  "-Ypatmat-exhaust-depth", "20",
  "-Ydelambdafy:method",
  "-Xmax-classfile-name", "100"
)

val commonSettings = Seq(
  organizationName := "Typelevel",
  startYear := Some(2018),
  licenses += ("Apache-2.0", new URL("https://www.apache.org/licenses/LICENSE-2.0.txt")),
  homepage := Some(url("https://github.com/gvolpe/console4cats")),
  libraryDependencies ++= Seq(
    Libraries.catsEffect,
    Libraries.scalaTest
  ),
  resolvers += "Apache public" at "https://repository.apache.org/content/groups/public/",
  scalacOptions ++= commonScalacOptions,
  scalafmtOnCompile := true,
  publishTo := {
    val sonatype = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at sonatype + "content/repositories/snapshots")
    else
      Some("releases" at sonatype + "service/local/staging/deploy/maven2")
  },
  publishMavenStyle := true,
  publishArtifact in Test := false,
  pomIncludeRepository := { _ => false },
  pomExtra :=
      <developers>
        <developer>
          <id>gvolpe</id>
          <name>Gabriel Volpe</name>
          <url>http://github.com/gvolpe</url>
        </developer>
      </developers>
)

lazy val noPublish = Seq(
  publish := {},
  publishLocal := {},
  publishArtifact := false,
  skip in publish := true
)

lazy val root = project.in(file("."))
  .aggregate(console4cats, microsite)
  .settings(noPublish)

lazy val console4cats = project.in(file("core"))
  .settings(commonSettings: _*)
  .enablePlugins(AutomateHeaderPlugin)

lazy val microsite = project.in(file("site"))
  .enablePlugins(MicrositesPlugin)
  .settings(commonSettings: _*)
  .settings(noPublish)
  .settings(
    micrositeName := "Console4Cats",
    micrositeDescription := "Console I/O for Cats Effect",
    micrositeAuthor := "Gabriel Volpe",
    micrositeGithubOwner := "gvolpe",
    micrositeGithubRepo := "console4cats",
    micrositeBaseUrl := "/console4cats",
    micrositeExtraMdFiles := Map(
      file("README.md") -> ExtraMdFileConfig(
        "index.md",
        "home",
        Map("title" -> "Home", "position" -> "0")
      )
    ),
    micrositeGitterChannel := true,
    micrositeGitterChannelUrl := "typelevel/cats-effect",
    micrositePushSiteWith := GitHub4s,
    micrositeGithubToken := sys.env.get("GITHUB_TOKEN"),
    fork in tut := true,
    scalacOptions in Tut --= Seq(
      "-Xfatal-warnings",
      "-Ywarn-unused-import",
      "-Ywarn-numeric-widen",
      "-Ywarn-dead-code",
      "-Xlint:-missing-interpolator,_",
    )
  )
  .dependsOn(console4cats)
