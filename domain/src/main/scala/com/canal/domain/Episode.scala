package com.canal.domain

case class Episode(
  titleId: String,
  parentTitleId: String,
  seasonNumber: Short,
  episodeNumber: Short
)