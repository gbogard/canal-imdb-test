package com.canal.infrastructure.db

import doobie._
import doobie.hikari._
import cats.effect._

object Config {
  val jdbcUrl: String = sys.env.getOrElse("JDBC_URL", "jdbc:sqlite:canal.db")

  def transactor[F[_]: Sync : Async : ContextShift]: Resource[F, HikariTransactor[F]] =
    for {
      ce <- ExecutionContexts.fixedThreadPool[F](32) // our connect EC
      be <- Blocker[F] // our blocking EC
      xa <- HikariTransactor.newHikariTransactor[F](
        "org.sqlite.JDBC",
        jdbcUrl,
        "",
        "",
        ce, // await connection here
        be // execute JDBC operations here
      )
    } yield xa

}
