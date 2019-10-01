import Dependencies._

ThisBuild / scalaVersion     := "2.12.10"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.canal"
ThisBuild / organizationName := "Canal+"

ThisBuild / resolvers += Resolver.sonatypeRepo("releases")
addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3")

lazy val root = (project in file("."))
  .settings(name := "Canal+ Test")

lazy val domain = (project in file("domain"))
  .settings(name :=  "domain")

lazy val dataPipelines = (project in file("data-pipelines"))
  .settings(name := "Data pipelines")
  .settings(libraryDependencies ++= Seq(akkaStreams, alpakkaCsv))
  .dependsOn(domain)

lazy val httpApi = (project in file("http-api"))
  .settings(name := "Http API")
  .settings(libraryDependencies ++= Seq(akkaStreams, akkaHttp))
  .dependsOn(domain, dataPipelines)

lazy val benchmarks = (project in file("benchmarks"))
  .settings(name := "Benchmarks")
  .dependsOn(domain, dataPipelines)