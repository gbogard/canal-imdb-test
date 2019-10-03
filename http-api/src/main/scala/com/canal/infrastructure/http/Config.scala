package com.canal.infrastructure.http

object Config {
  val port: Int = sys.env.getOrElse("API_PORT", "9000").toInt
  val host: String = sys.env.getOrElse("API_HOST", "0.0.0.0")
  val maxLimitForQueries: Int = 100
}
