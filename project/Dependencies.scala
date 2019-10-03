import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.8"
  lazy val akkaStreams = "com.typesafe.akka" %% "akka-stream" % "2.5.25"
  lazy val alpakkaCsv = "com.lightbend.akka" %% "akka-stream-alpakka-csv" % "1.1.1"
  lazy val akkaHttp = "com.typesafe.akka" %% "akka-http" % "10.1.10"
  lazy val scalaMeter = "com.storm-enroute" %% "scalameter" % "0.18"
  lazy val doobie = "org.tpolecat" %% "doobie-core" % "0.8.4"
  lazy val doobieHikari = "org.tpolecat" %% "doobie-hikari" % "0.8.4"
  lazy val sqlite = "org.xerial" % "sqlite-jdbc" % "3.28.0"
  lazy val circe = Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic"
  ).map(_ % "0.12.1") :+ "de.heikoseeberger" %% "akka-http-circe" % "1.29.1"
}
