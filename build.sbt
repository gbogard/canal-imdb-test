import Dependencies._

ThisBuild / scalaVersion := "2.12.10"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.canal"
ThisBuild / organizationName := "Canal+"
ThisBuild / resolvers += Resolver.sonatypeRepo("releases")

val kindProjector = addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.10.3")

lazy val root = (project in file("."))
  .settings(name := "Canal+ Test")
  .aggregate(databasePopulator, httpApi)

lazy val domain = (project in file("domain"))

lazy val databaseInfrastructure = (project in file("database-infrastructure"))
  .settings(libraryDependencies ++= Seq(akkaStreams, doobie, doobieHikari, sqlite))
  .dependsOn(domain)

lazy val httpApi = (project in file("http-api"))
  .settings(name := "Http API")
  .settings(kindProjector)
  .settings(
    libraryDependencies ++= Seq(akkaStreams, akkaHttp, scalaTest % "test") ++ circe
  )
  .dependsOn(domain, databaseInfrastructure)

lazy val databasePopulator = (project in file("database-populator"))
  .settings(
    libraryDependencies ++= Seq(
      akkaStreams,
      alpakkaCsv, 
      scalaTest % "test"
    )
  )
  .settings(kindProjector)
  .dependsOn(domain, databaseInfrastructure)
