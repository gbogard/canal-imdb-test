package com.canal.populator

import akka.stream.Attributes
import akka.stream.scaladsl._
import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._
import com.canal.infrastructure.db.repositories._
import com.canal.infrastructure.db.{Config => DbConfig}
import com.canal.populator.Materializer._
import doobie.util.transactor.Transactor

object Main extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    DbConfig
      .transactor[IO]
      .use(xa => CLI.parseArgumentsAndLaunch(args, migrateAndPopulate(_)(xa))) *> IO {
      system.terminate()
      ExitCode.Success
    }

  private def migrateAndPopulate(
      arguments: Seq[String]
  )(implicit xa: Transactor[IO]): IO[Unit] =
    Schema.migrateDatabase(xa) *> IO {
      println(CLI.Messages.onSchemaMigrationEnd)
    } *> (
      if (arguments.nonEmpty) {
        IO(println(CLI.Messages.onPopulationStart)) *>
          IO.fromFuture(
            IO(population(arguments).runWith(Sink.last))
          )
      } else IO.unit
    )

  private def population(
      arguments: Seq[String]
  )(implicit xa: Transactor[IO]) = {
    val titleRepository = new TitleRepositoryInterpreter[IO]()
    val personRepository = new PersonRepositoryInterpreter[IO]()
    val principalRepository = new PrincipalRepositoryInterpreter[IO]()
    val episodeRepository = new EpisodeRepositoryInterpreter[IO]()

    val populateTitles =
      if (arguments.contains(CLI.Arguments.titlesArgument))
        Populate
          .insertTitles(titleRepository)
          .via(logCompletion("Insert titles"))
      else Source.empty

    val populatePeople =
      if (arguments.contains(CLI.Arguments.peopleArgument))
        Populate
          .insertPeople(personRepository)
          .via(logCompletion("Insert people"))
      else Source.empty

    val populatePrincipals =
      if (arguments.contains(CLI.Arguments.principalsArgument))
        Populate
          .insertPrincipals(principalRepository)
          .via(logCompletion("Insert principals"))
      else Source.empty

    val populateEpisodes =
      if (arguments.contains(CLI.Arguments.episodesArgument))
        Populate
          .insertEpisodes(episodeRepository)
          .via(logCompletion("Insert episodes"))
      else Source.empty

    Source(
      List(populateTitles, populatePeople, populatePrincipals, populateEpisodes)
    ).flatMapConcat(identity)
  }

  private def logCompletion[T](name: String) =
    Flow
      .apply[T]
      .log(name)
      .addAttributes(
        Attributes.logLevels(
          onElement = Attributes.LogLevels.Off,
          onFailure = Attributes.LogLevels.Error,
          onFinish = Attributes.LogLevels.Info
        )
      )

}
