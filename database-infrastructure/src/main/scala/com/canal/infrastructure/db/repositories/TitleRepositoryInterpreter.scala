package com.canal.infrastructure.db.repositories

import cats.effect.Sync
import cats.implicits._
import doobie._
import doobie.implicits._
import com.canal.domain._
import com.canal.domain.repositories.TitleRepository
import com.canal.infrastructure.db._

class TitleRepositoryInterpreter[F[_]: Sync]()(implicit xa: Transactor[F])
    extends TitleRepository[F] {
  import TitleRepositoryInterpreter._

  def insertTitles(titles: Seq[Title]): F[Unit] =
    titles.toList.traverse(insertSingleTitleQuery)
      .transact(xa)
      .map(_ => ())

  def titlesWithLargestEpisodeCount(limit: Int): F[List[TitleWithEpisodeCount]] =
    titlesWithLargestEpisodeCountQuery(limit).transact(xa)
}

object TitleRepositoryInterpreter {

  def titlesWithLargestEpisodeCountQuery(limit: Int): doobie.ConnectionIO[List[TitleWithEpisodeCount]] =
    sql"""
    SELECT t.*, COUNT(1) as episode_count FROM episodes e
    JOIN titles t ON t.id = parent_title_id
    GROUP BY e.parent_title_id
    ORDER BY episode_count DESC
    LIMIT $limit
    """.query[TitleWithEpisodeCount].to[List]

  def insertSingleTitleQuery(title: Title): doobie.ConnectionIO[Int] =
    sql"""
    INSERT OR IGNORE INTO titles VALUES(
      ${title.id},
      ${title.titleType},
      ${title.primaryTitle},
      ${title.originalTitle},
      ${title.isAdult},
      ${title.startYear},
      ${title.endYear},
      ${title.runtimeMinutes},
      ${title.genres}
    )
    """.update.run
}
