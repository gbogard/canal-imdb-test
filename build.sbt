import Dependencies._

ThisBuild / scalaVersion     := "2.12.10"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.canal"
ThisBuild / organizationName := "Canal+"

ThisBuild / resolvers += Resolver.sonatypeRepo("releases")

lazy val root = (project in file("."))
  .settings(name := "Canal+ Test")
  .aggregate(dataPipelines, httpApi)

lazy val domain = (project in file("domain"))
  .settings(name :=  "domain")

lazy val dataPipelines = (project in file("data-pipelines"))
  .settings(name := "Data pipelines")
  .settings(libraryDependencies ++= Seq(akkaStreams, alpakkaCsv, scalaTest % "test"))
  .settings(addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3"))
  .dependsOn(domain)

lazy val httpApi = (project in file("http-api"))
  .settings(name := "Http API")
  .settings(libraryDependencies ++= Seq(akkaStreams, akkaHttp, scalaTest % "test"))
  .dependsOn(domain, dataPipelines)

lazy val benchmarks = (project in file("benchmarks"))
  .settings(name := "Benchmarks")
  .dependsOn(domain, dataPipelines)