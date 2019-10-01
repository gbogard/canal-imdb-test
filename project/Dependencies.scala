import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.8"
  lazy val akkaStreams = "com.typesafe.akka" %% "akka-stream" % "2.5.25" 
  lazy val alpakkaCsv = "com.lightbend.akka" %% "akka-stream-alpakka-csv" % "1.1.1"
  lazy val akkaHttp = "com.typesafe.akka" %% "akka-http"   % "10.1.10"  
}
