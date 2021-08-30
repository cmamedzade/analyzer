name := "HumidityAnalyzer"

version := "0.1"



scalaVersion := "2.13.6"
val AkkaVersion = "2.6.16"



libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % AkkaVersion,
  "com.typesafe.akka" %% "akka-stream-testkit" % AkkaVersion % Test,
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test,
  "org.scalatest" %% "scalatest" % "3.1.4" % Test,
  "org.scalatestplus" %% "mockito-3-4" % "3.2.9.0" % "test",
  "ch.qos.logback" % "logback-classic" % "1.2.5",
)

