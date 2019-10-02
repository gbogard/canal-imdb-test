package com.canal.domain

case class Episode(
  titleId: String,
  parentTitleId: String,
  seasonNumber: Option[Int],
  episodeNumber: Option[Int]
)