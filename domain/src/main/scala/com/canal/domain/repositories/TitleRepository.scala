package com.canal.domain.repositories

import com.canal.domain.{Title, TitleWithEpisodeCount}

trait TitleRepository[F[_]] {

  def insertTitles(titles: Seq[Title]): F[Unit]

  def titlesWithLargestEpisodeCount(limit: Int): F[List[TitleWithEpisodeCount]]
}