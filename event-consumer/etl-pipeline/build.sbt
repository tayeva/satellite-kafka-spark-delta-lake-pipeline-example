
ThisBuild / scalaVersion     := "2.13.10"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.poc"
ThisBuild / organizationName := "poc"

lazy val root = (project in file("."))
  .settings(
    name := "etl-pipeline",
    run / fork := true,
    run / javaOptions ++= Seq(
    "--add-opens=java.base/sun.nio.ch=ALL-UNNAMED"
    ),
    libraryDependencies ++= Seq(
      "com.google.flatbuffers" % "flatbuffers-java" % "23.5.26",
      "io.delta" %% "delta-core" % "2.4.0",
      "org.apache.spark" %% "spark-core" % "3.4.0",
      "org.apache.spark" %% "spark-sql" % "3.4.0",
      "org.apache.spark" %% "spark-streaming" % "3.4.0",
      "org.apache.spark" %% "spark-streaming-kafka-0-10" % "3.4.0",
      "org.apache.kafka" % "kafka-clients" % "3.4.1",
    )
  )
