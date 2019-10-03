package com.canal.domain.repositories

import com.canal.domain.Episode

trait EpisodeRepository[F[_]] {
  
  def insertEpisodes(episodes: Seq[Episode]): F[Unit]
}