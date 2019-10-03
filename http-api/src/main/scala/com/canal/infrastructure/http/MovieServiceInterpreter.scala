package com.canal.infrastructure.http

import akka.NotUsed
import akka.stream.scaladsl._
import cats.effect.IO
import com.canal.domain.errors.LimitTooHigh
import com.canal.domain.repositories._
import com.canal.domain.{MovieService, PersonWithJob, TitleWithEpisodeCount}

class MovieServiceInterpreter()(
    implicit personRepository: PersonRepository[IO],
    titleRepository: TitleRepository[IO]
) extends MovieService[Source[*, NotUsed]] {

  def principalsForMovieName(name: String): Source[PersonWithJob, NotUsed] =
    Source
      .fromFuture(
        personRepository.principalsForMovieName(name).unsafeToFuture()
      )
      .flatMapConcat(Source(_))

  def tvSeriesWithGreatestNumberOfEpisodes(limit: Int): Source[TitleWithEpisodeCount, NotUsed] =
    if (limit < Config.maxLimitForQueries)
        Source.fromFuture(
          titleRepository.titlesWithLargestEpisodeCount(limit).unsafeToFuture()
        ).flatMapConcat(Source(_))
    else
      Source.failed(new LimitTooHigh(Config.maxLimitForQueries))
}
