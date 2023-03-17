name := "stochastic-lexemes"

version := "1.0.1"

scalaVersion := "2.13.10"

lazy val akkaVersion = "2.8.0"
lazy val akkaHttpVersion = "10.5.0"
lazy val scalaTestVersion = "3.2.15" // 3.0.5
lazy val sprayJsonVersion = "1.3.6"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.8.0",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "org.scalatest" %% "scalatest" % scalaTestVersion % "test",
  "org.scalactic" %% "scalactic" % scalaTestVersion,
  "org.scalactic" %% "scalactic" % scalaTestVersion % "test",
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "io.spray" %% "spray-json" % sprayJsonVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test
)
