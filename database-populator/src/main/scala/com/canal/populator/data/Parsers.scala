package com.canal.populator.data

import com.canal.domain._

object Parsers {

  def mapToTitle(input: Map[String, String]): Option[Title] =
    for {
      id <- input.get("tconst")
      titleType <- input.get("titleType").flatMap(TitleType(_))
      primaryTitle <- input.get("primaryTitle")
      originalTitle <- input.get("originalTitle")
      isAdult <- input.get("isAdult").flatMap(boolean)
      startYear <- input.get("startYear")
      endYear <- input.get("endYear")
      runtimeMinutes <- input.get("runtimeMinutes")
      genres <- input.get("genres").map(vector)
    } yield Title(
      id,
      titleType,
      primaryTitle,
      originalTitle,
      isAdult,
      optional(startYear).map(_.toShort),
      optional(endYear).map(_.toShort),
      optional(runtimeMinutes).map(_.toInt),
      genres
    )

  def mapToPerson(input: Map[String, String]): Option[Person] =
    for {
      id <- input.get("nconst")
      primaryName <- input.get("primaryName")
      birthYear <- input.get("birthYear")
      deathYear <- input.get("deathYear")
      primaryProfession <- input.get("primaryProfession").map(vector)
    } yield Person(
      id,
      primaryName,
      optional(birthYear).map(_.toShort),
      optional(deathYear).map(_.toShort),
      primaryProfession,
    )

  def mapToPincipal(input: Map[String, String]): Option[Principal] =
    for {
      titleId <- input.get("tconst")
      ordering <- input.get("ordering")
      personId <- input.get("nconst")
      category <- input.get("category")
      job <- input.get("job")
      characters <- input.get("characters")
    } yield Principal(
      titleId,
      ordering.toShort,
      personId,
      category,
      optional(job),
      optional(characters)
    )
  
  def mapToEpisode(input: Map[String, String]): Option[Episode] = for {
    titleId <- input.get("tconst")
    parentTitleId <- input.get("parentTconst")
    seasonNumber <- input.get("seasonNumber")
    episodeNumber <- input.get("episodeNumber")
  } yield Episode(
    titleId,
    parentTitleId,
    optional(seasonNumber).map(_.toInt),
    optional(episodeNumber).map(_.toInt)
  )

  def mapToRating(input: Map[String, String]): Option[Rating] = for {
    titleId <- input.get("tconst")
    averageRating <- input.get("averageRating")
    numVotes <- input.get("numVotes")
  } yield Rating(
    titleId,
    averageRating.toFloat,
    numVotes.toLong
  )

  private def optional(field: String): Option[String] = field match {
    case "\\N" => None
    case value => Some(value)
  }

  private def boolean(field: String): Option[Boolean] = field match {
    case "0" => Some(false)
    case "1" => Some(true)
    case _   => None
  }

  private def vector(field: String): Vector[String] = field.split(",").toVector

}
