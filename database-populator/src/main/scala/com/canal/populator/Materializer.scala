package com.canal.populator

import akka.actor.ActorSystem
import akka.stream._

object Materializer {
  implicit val system: ActorSystem = ActorSystem("imdb-populator")
  implicit val materializer: Materializer = ActorMaterializer()
}