package com.canal.infrastructure.http

import akka.http.scaladsl.Http
import cats.effect._
import cats.implicits._
import com.canal.infrastructure.db.repositories.{PersonRepositoryInterpreter, TitleRepositoryInterpreter}
import com.canal.infrastructure.http.Materializer._
import doobie.util.transactor.Transactor

import scala.concurrent.ExecutionContext.Implicits.global

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] =
    com.canal.infrastructure.db.Config
      .transactor[IO]
      .use(launch(_))

  private def launch(implicit transactor: Transactor[IO]) = IO.fromFuture(IO {
    implicit val personRepository = new PersonRepositoryInterpreter[IO]()
    implicit val titleRepository = new TitleRepositoryInterpreter[IO]()
    implicit val movieService = new MovieServiceInterpreter()

    val route = new Routes().route
    Http().bindAndHandle(route, Config.host, Config.port)
  }) *> IO {
    println(s"Server is running on ${Config.host}:${Config.port}")
  } *> IO.never

}
