package com.canal.infrastructure.db.repositories

import cats.effect.Sync
import cats.implicits._
import com.canal.domain._
import com.canal.domain.repositories.EpisodeRepository
import doobie._
import doobie.implicits._

class EpisodeRepositoryInterpreter[F[_]: Sync]()(implicit xa: Transactor[F]) extends EpisodeRepository[F] {
  override def insertEpisodes(episodes: Seq[Episode]): F[Unit] =
    episodes.toList.traverse(insertSingleEpisodeQuery).transact(xa).map(_ => ())

  private def insertSingleEpisodeQuery(episode: Episode) =
    sql"""
    INSERT OR IGNORE INTO episodes VALUES(
      ${episode.titleId},
      ${episode.parentTitleId},
      ${episode.seasonNumber},
      ${episode.episodeNumber}
    )
    """.update.run
}
