package com.canal.datapipelines

import com.canal.domain._
import akka.stream.scaladsl._
import akka.NotUsed

class MovieServiceInterpreter(
    parallelism: Int = 8
) extends MovieService[Source[?, NotUsed]] {

  def principalsForMovieName(name: String): Source[PersonWithJob, NotUsed] =
    titleByName(name)
      .flatMapConcat(title => principalsByTitleId(title.id))
      .flatMapMerge(parallelism, principal => {
        peopleById(principal.personId).map(
          person => PersonWithJob.fromPrincipalAndPerson(person, principal)
        )
      })
      .mapMaterializedValue(_ => NotUsed)

  def tvSeriesWithGreatestNumberOfEpisodes(limit: Int): Source[Title, NotUsed] =
    sortedEpisodeCountByParentTitleId
      .flatMapConcat(Source(_))
      .take(limit.toLong)
      .flatMapMerge(parallelism, tuple => {
        titleById(tuple._1)
      })
      .mapMaterializedValue(_ => NotUsed)

  private def titleByName(name: String): Source[Title, _] =
    RawSources.titlesSource
      .filter(
        title => title.primaryTitle == name || title.originalTitle == name
      )
      .take(1)

  def titleById(id: String): Source[Title, _] =
    RawSources.titlesSource
      .filter(_.id.trim == id.trim)
      .async
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
      .async
      .take(1)

  private def sortedEpisodeCountByParentTitleId
      : Source[List[(String, Int)], _] =
    RawSources.episodesSource
      .fold(Map.empty[String, Int])({
        case (map, episode) =>
          map.updated(
            episode.parentTitleId,
            map.getOrElse(episode.parentTitleId, 0) + 1
          )
      })
      .map(map => map.toList.sortBy(-_._2))

  private def stringIdToNumber(id: String): Long = id.drop(2).toLong
}
