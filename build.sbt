name := "music-metadata-service"
version := "1.0"
scalaVersion := "2.13.12"

libraryDependencies ++= Seq(
  // Akka HTTP for building the REST API
  "com.typesafe.akka" %% "akka-http" % "10.2.10",
  // Akka Streams for handling streams
  "com.typesafe.akka" %% "akka-stream" % "2.6.20",
  // Akka Actor for the ActorSystem
  "com.typesafe.akka" %% "akka-actor" % "2.6.20",
  // JSON support for Akka HTTP
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.2.10",
  // ScalaTest for unit testing
  "org.scalatest" %% "scalatest" % "3.2.15" % Test
)

Compile / run / mainClass := Some("Main")