lazy val CatsVersion       = "2.1.1"
lazy val CatsEffectVersion = "2.1.3"
lazy val Fs2Version        = "2.3.0"
lazy val ScalaTestVersion  = "3.0.7"
lazy val ScalaCheckVersion = "1.13.4"

lazy val suprNationTriangles = (project in file(".")).settings(
  Seq(
    name := "triangles-paths",
    version := "1.0",
    scalaVersion := "2.12.11",
    mainClass in Compile := Some("com.triangles.Main"),
    scalacOptions := Seq(
      "-deprecation",
      "-encoding",
      "UTF-8",
      "-feature",
      "-deprecation",
      "-unchecked",
      "-language:postfixOps",
      "-language:existentials",
      "-language:higherKinds",
      "-Ypartial-unification"
    ),
    libraryDependencies ++= Seq(
      "org.typelevel"              %% "cats-effect"     % CatsEffectVersion,
      "co.fs2"                     %% "fs2-core"        % Fs2Version,
      "co.fs2"                     %% "fs2-io"          % Fs2Version,
      "org.scalatest"              %% "scalatest"       % ScalaTestVersion % Test,
      "org.scalacheck"             %% "scalacheck"      % ScalaCheckVersion % Test
    )
  )
)
