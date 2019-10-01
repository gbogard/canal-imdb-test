package com.canal.domain

case class Title(
  id: String,
  titleType: TitleType,
  primaryTitle: String,
  originalTitle: String,
  isAdult: Boolean,
  startYear: Option[Short],
  endYear: Option[Short],
  runtimeMinutes: Option[Short],
  genres: Vector[String]
)