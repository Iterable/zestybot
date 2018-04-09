import Dependencies._

enablePlugins(DockerPlugin)

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
    ),
    dockerfile in docker := {
      val jarFile: File = sbt.Keys.`package`.in(Compile, packageBin).value
      val classpath = (managedClasspath in Compile).value
      val mainclass = mainClass.in(Compile, packageBin).value.getOrElse(sys.error("Expected exactly one main class"))
      val jarTarget = s"/app/${jarFile.getName}"
      // Make a colon separated classpath with the JAR file
      val classpathString = classpath.files.map("/app/" + _.getName)
        .mkString(":") + ":" + jarTarget
      new Dockerfile {
        // Base image
        from("openjdk:8-jre")
        // Add all files on the classpath
        add(classpath.files, "/app/")
        // Add the JAR file
        add(jarFile, jarTarget)
        // On launch run Java with the classpath and the main class
        entryPoint("java", "-cp", classpathString, mainclass)
      }
    }
  )
