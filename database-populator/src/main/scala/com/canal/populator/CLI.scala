package com.canal.populator

import cats.effect.IO
import com.canal.infrastructure.db.{Config => DBConfig}
import cats.implicits._

object CLI {
  object Arguments {
    val titlesArgument = "titles"
    val peopleArgument = "people"
    val principalsArgument = "principals"
    val episodesArgument = "episodes"
    val validArguments =
      List(titlesArgument, peopleArgument, principalsArgument, episodesArgument)
  }

  object Flags {
    val help = "--help"
    val only = "--only"
  }

  object Messages {
    val separator: String = "======================================="
    def start(migrationArguments: Seq[String], jdbcUrl: String): String =
      s"""
        |$separator
        |Database populator is starting
        |Database schema will be created if necessary.
        |Database will be populated with the following entities : ${migrationArguments
           .mkString(" ")}
        |$separator
        |JDBC_URL : $jdbcUrl
        |""".stripMargin
    val onSchemaMigrationEnd: String = "Finished Schema migration"
    val onPopulationStart: String =
      s"""
        |$separator
        |Starting database population.
        |This will take a while. Go grab a coffee!
        |""".stripMargin
    val onFinish: String = "Everything OK. Bye now!"
    val helpMessage: String =
      s"""
         |Database populator help
         |
         |The program can be called with two flags
         |  --help
         |    will display this message
         |  --only <entity>*
         |    will populate the database with only the selected entities.
         |    e.g.: --only titles episodes
         |
         |    Valid entities are:
         |      - ${Arguments.titlesArgument}
         |      - ${Arguments.peopleArgument}
         |      - ${Arguments.principalsArgument}
         |      - ${Arguments.episodesArgument}
         |
         |When called with no argument, the program will populate the database with all available entities.
         |""".stripMargin
    val invalidInput = "Invalid input. type --help for help"
  }

  def parseArgumentsAndLaunch(
      args: List[String],
      program: Seq[String] => IO[Unit]
  ): IO[Unit] =
    args.map(_.trim) match {
      case Flags.help :: Nil => IO(println(Messages.helpMessage))
      case Flags.only :: tail
        if tail.forall(Arguments.validArguments.contains(_)) =>
        IO(println(Messages.start(tail, DBConfig.jdbcUrl))) *> program(tail) *> IO(
          println(Messages.onFinish)
        )
      case Nil =>
        IO(println(Messages.start(Arguments.validArguments, DBConfig.jdbcUrl))) *> program(
          Arguments.validArguments
        ) *> IO(println(Messages.onFinish))
      case _ => IO(println(Messages.invalidInput))
    }

}
