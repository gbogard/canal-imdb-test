package com.canal.domain

case class Title(
  id: String,
  titleType: TitleType,
  primaryTitle: String,
  originalTitle: String,
  isAdult: Boolean,
  startYear: Short,
  endYear: Option[Short],
  runtimeMinutes: Short,
  genres: Vector[String]
)