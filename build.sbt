import sbt.Keys._
import sbt._
import uk.gov.hmrc.DefaultBuildSettings._
import uk.gov.hmrc.SbtArtifactory
import uk.gov.hmrc.versioning.SbtGitVersioning
import uk.gov.hmrc.versioning.SbtGitVersioning.majorVersion

val appName: String = "slug-builder"

lazy val slugBuilder = Project(appName, file("."))
  .enablePlugins(Seq(SbtAutoBuildPlugin, SbtGitVersioning, SbtArtifactory): _*)
  .settings(scalaSettings: _*)
  .settings(defaultSettings(): _*)
  .settings(
    majorVersion := 0,
    scalaVersion := "2.11.12",
    libraryDependencies ++= compile ++ test,
    retrieveManaged := true,
    assemblySettings,
    evictionWarningOptions in update := EvictionWarningOptions.default.withWarnScalaVersionEviction(false),
    resolvers += Resolver.bintrayRepo("hmrc", "releases"),
    resolvers += Resolver.jcenterRepo
  )

val compile = Seq(
  "org.typelevel"     %% "cats-core"              % "1.0.1",
  "com.typesafe.play" %% "play-ahc-ws-standalone" % "1.1.2"
)

val test = Seq(
  "org.scalatest"  %% "scalatest"  % "3.0.5"  % Test,
  "org.scalacheck" %% "scalacheck" % "1.14.0" % Test,
  "org.pegdown"    % "pegdown"     % "1.4.2"  % Test,
  "org.scalamock"  %% "scalamock"  % "4.1.0"  % Test
)

val assemblySettings = Seq(
  assemblyJarName in assembly := "slug-builder.jar",
  assemblyMergeStrategy in assembly := {
    case PathList("org", "apache", "commons", "logging", xs @ _*) => MergeStrategy.first
    case PathList("play", "core", "server", xs @ _*)              => MergeStrategy.first
    case x                                                        => (assemblyMergeStrategy in assembly).value(x)
  },
  artifact in (Compile, assembly) := {
    val art = (artifact in (Compile, assembly)).value
    art.copy(`classifier` = Some("assembly"))
  }
)
