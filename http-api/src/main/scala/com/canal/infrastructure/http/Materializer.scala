package com.canal.infrastructure.http

import akka.actor.ActorSystem
import akka.stream._

object Materializer {
  implicit val system: ActorSystem = ActorSystem("imdb-api")
  implicit val materializer: Materializer = ActorMaterializer()
}