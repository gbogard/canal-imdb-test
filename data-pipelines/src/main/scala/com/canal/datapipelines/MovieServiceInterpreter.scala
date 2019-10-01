package com.canal.datapipelines

import com.canal.domain._
import akka.stream.scaladsl._
import akka.NotUsed

class MovieServiceInterpreter(
  parallelism: Int = 8
) extends MovieService[Source[?, NotUsed]] {

  def principalsForMovieName(name: String): Source[PersonWithJob, NotUsed] = {
    titleByName(name)
      .flatMapConcat(title => principalsByTitleId(title.id))
      .flatMapMerge(parallelism, principal => {
        peopleById(principal.personId).map(
          person => PersonWithJob.fromPrincipalAndPerson(person, principal)
        )
      })
      .mapMaterializedValue(_ => NotUsed)
  }

  def tvSeriesWithGreatestNumberOfEpisodes(limit: Int): Source[Title, NotUsed] =
    ???

  private def titleByName(name: String): Source[Title, _] =
    RawSources.titlesSource
      .filter(title => title.primaryTitle == name || title.originalTitle == name)
      .take(1)

  private def principalsByTitleId(id: String): Source[Principal, _] =
    /*
     The principals TSV files appears to be sorted by title id, so we can
     search only consecutive results instead of scanning the entire file.
     */
    RawSources.principalsSource
      .drop(stringIdToNumber(id) - 1)
      .dropWhile(_.titleId != id)
      .takeWhile(_.titleId == id)

  private def peopleById(id: String): Source[Person, _] =
    RawSources.peopleSource
      .filter(_.id == id)
      .take(1)

  private def stringIdToNumber(id: String): Long = id.drop(2).toLong
}
