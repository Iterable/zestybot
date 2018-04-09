import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.iterable",
      scalaVersion := "2.12.4",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "zestybot",
    libraryDependencies ++= Seq(
      scalaTest % Test,
      "com.softwaremill.sttp" %% "core" % "1.1.4",
      "com.softwaremill.sttp" %% "circe" % "1.1.4",
      "com.softwaremill.sttp" %% "akka-http-backend" % "1.1.4",
      "io.circe" %% "circe-generic" % "0.9.3",
      "io.circe" %% "circe-parser" % "0.9.3"
    )
  )
