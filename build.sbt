import com.scalapenos.sbt.prompt.SbtPrompt.autoImport._
import com.scalapenos.sbt.prompt._
import Dependencies._
import microsites.ExtraMdFileConfig

name := """console4cats"""

organization in ThisBuild := "dev.profunktor"

crossScalaVersions in ThisBuild := Seq("2.12.10", "2.13.5")

sonatypeProfileName := "dev.profunktor"

promptTheme := PromptTheme(
  List(
    text("[sbt] ", fg(105)),
    text(_ => "console4cats", fg(15)).padRight(" λ ")
  )
)

val commonSettings = Seq(
  organizationName := "ProfunKtor",
  startYear := Some(2018),
  licenses += ("Apache-2.0", new URL("https://www.apache.org/licenses/LICENSE-2.0.txt")),
  headerLicense := Some(HeaderLicense.ALv2("2018-2020", "ProfunKtor")),
  homepage := Some(url("https://console4cats.profunktor.dev/")),
  libraryDependencies ++= Seq(
        Deps.catsEffect,
        Deps.munit,
        CompilerPlugins.bm4,
        CompilerPlugins.contextApplied,
        CompilerPlugins.kindProjector
      ),
  testFrameworks += new TestFramework("munit.Framework"),
  resolvers += "Apache public" at "https://repository.apache.org/content/groups/public/",
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
          <url>https://github.com/gvolpe</url>
        </developer>
      </developers>
)

lazy val noPublish = Seq(
  publish := {},
  publishLocal := {},
  publishArtifact := false,
  skip in publish := true
)

lazy val root = project
  .in(file("."))
  .aggregate(console4cats, microsite)
  .settings(noPublish)

lazy val console4cats = project
  .in(file("core"))
  .settings(commonSettings: _*)
  .enablePlugins(AutomateHeaderPlugin)

lazy val microsite = project
  .in(file("site"))
  .enablePlugins(MicrositesPlugin)
  .settings(commonSettings: _*)
  .settings(noPublish)
  .settings(
    micrositeName := "Console4Cats",
    micrositeDescription := "Console I/O for Cats Effect",
    micrositeAuthor := "ProfunKtor",
    micrositeGithubOwner := "profunktor",
    micrositeGithubRepo := "console4cats",
    micrositeBaseUrl := "",
    micrositeExtraMdFiles := Map(
          file("README.md") -> ExtraMdFileConfig(
                "index.md",
                "home",
                Map("title" -> "Home", "position" -> "0")
              ),
          file("CODE_OF_CONDUCT.md") -> ExtraMdFileConfig(
                "CODE_OF_CONDUCT.md",
                "page",
                Map("title" -> "Code of Conduct")
              )
        ),
    micrositeExtraMdFilesOutput := (resourceManaged in Compile).value / "jekyll",
    micrositeGitterChannel := true,
    micrositeGitterChannelUrl := "typelevel/cats-effect",
    micrositePushSiteWith := GitHub4s,
    micrositeGithubToken := sys.env.get("GITHUB_TOKEN")
  )
  .dependsOn(console4cats)

// CI build
addCommandAlias("buildConsole4Cats", ";clean;+test;mdoc")
