package com.canal.infrastructure.http

import akka.http.scaladsl.Http
import cats.effect._
import com.canal.infrastructure.db.repositories.{PersonRepositoryInterpreter, TitleRepositoryInterpreter}
import com.canal.infrastructure.http.Materializer._
import doobie.util.transactor.Transactor

import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.StdIn

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] =
    com.canal.infrastructure.db.Config.transactor[IO].use(launch(_)).map(_ => ExitCode.Success)

  def launch(implicit transactor: Transactor[IO]): IO[Unit] = IO {
    implicit val personRepository = new PersonRepositoryInterpreter[IO]()
    implicit val titleRepository = new TitleRepositoryInterpreter[IO]()
    implicit val movieService = new MovieServiceInterpreter()

    val route = new Routes().route

    val bindingFuture = Http().bindAndHandle(route, Config.host, Config.port)

    println(s"Server online at http://${Config.host}:${Config.port}/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate()) // and shutdown when done
  }

}
