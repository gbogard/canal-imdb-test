package com.canal.datapipelines

import akka.actor.ActorSystem
import akka.stream._

object Materializer {
  implicit val system = ActorSystem("imdb-data-pipelines")
  implicit val materializaer: Materializer = ActorMaterializer()
}