package com.canal.domain

case class TitleWithEpisodeCount(
  id: String,
  titleType: TitleType,
  primaryTitle: String,
  originalTitle: String,
  isAdult: Boolean,
  startYear: Option[Short],
  endYear: Option[Short],
  runtimeMinutes: Option[Int],
  genres: Vector[String],
  episodeCount: Int,
)