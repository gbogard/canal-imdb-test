package com.canal.populator

import akka.stream.scaladsl._
import cats.effect.IO
import com.canal.domain.repositories.{EpisodeRepository, PersonRepository, PrincipalRepository, TitleRepository}
import com.canal.populator.data.RawSources

object Populate {

  def insertTitles(titleRepository: TitleRepository[IO]): Source[Unit, _] =
    RawSources.titlesSource
      .grouped(Config.insertionChunkSize)
      .mapAsyncUnordered(Config.insertionParallelism)(
        titleRepository.insertTitles(_).unsafeToFuture()
      )


  def insertPeople(personRepository: PersonRepository[IO]): Source[Unit, _] =
    RawSources.peopleSource
      .grouped(Config.insertionChunkSize)
      .mapAsyncUnordered(Config.insertionParallelism)(
        personRepository.insertPeople(_).unsafeToFuture()
      )

  def insertPrincipals(principalRepository: PrincipalRepository[IO]): Source[Unit, _] =
    RawSources.principalsSource
      .grouped(Config.insertionChunkSize)
      .mapAsyncUnordered(Config.insertionParallelism)(
        principalRepository.insertPrincipals(_).unsafeToFuture()
      )

  def insertEpisodes(episodeRepository: EpisodeRepository[IO]): Source[Unit, _] =
    RawSources.episodesSource
      .grouped(Config.insertionChunkSize)
      .mapAsyncUnordered(Config.insertionParallelism)(
        episodeRepository.insertEpisodes(_).unsafeToFuture()
      )
}
