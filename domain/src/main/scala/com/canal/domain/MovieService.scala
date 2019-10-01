package com.canal.domain

trait MovieService[F[_]] {
 
  def principalsForMovieName(name: String): F[Principal]
  def tvSeriesWithGreatestNumberOfEpisodes(limit: Int): F[Title]

}