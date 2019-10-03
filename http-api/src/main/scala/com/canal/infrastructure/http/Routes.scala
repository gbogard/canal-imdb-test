package com.canal.infrastructure.http

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import Codecs._
import akka.NotUsed
import akka.http.scaladsl.common.{EntityStreamingSupport, JsonEntityStreamingSupport}
import akka.stream.scaladsl.Source
import com.canal.domain.MovieService
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._

class Routes()(
    implicit service: MovieService[Source[*, NotUsed]]
) {

  implicit private val jsonStreamingSupport: JsonEntityStreamingSupport = EntityStreamingSupport.json()

  private val healthcheck = pathSingleSlash {
    get {
      complete(HttpEntity("Canal+ IMDB Service is healthy!"))
    }
  }

  private val castPerTitle = path("titles" / "cast") {
    parameter("title") { title =>
      complete(
        service.principalsForMovieName(title)
      )
    }
  }

  private val highestNumberOfEpisodes = path("titles" / "countEpisodes") {
    parameter("limit".as[Int] ? 10) { limit =>
      complete(
        service.tvSeriesWithGreatestNumberOfEpisodes(limit)
      )
    }
  }

  val route: Route = healthcheck ~ castPerTitle ~ highestNumberOfEpisodes
}
