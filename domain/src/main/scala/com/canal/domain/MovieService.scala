package com.canal.domain

trait MovieService[F[_]] {
 
  def principalsForMovieName(name: String): F[PersonWithJob]
  def tvSeriesWithGreatestNumberOfEpisodes(limit: Int): F[TitleWithEpisodeCount]

}