package com.canal.domain

sealed trait TitleType {
  val id: String
}

object TitleType {
  case object Movie extends TitleType {
    val id = "movie"
  }
  case object Short extends TitleType {
    val id: String = "short"
  }
  case object Series extends TitleType {
    val id: String = "tvseries"
  }
  case object Episode extends TitleType {
    val id: String = "tvepisode"
  }
  case object Video extends TitleType {
    val id: String = "video"
  }

  def apply(id: String): Option[TitleType] = id match {
    case Movie.id => Some(Movie)
    case Short.id => Some(Short)
    case Series.id => Some(Series)
    case Episode.id => Some(Episode)
    case Video.id => Some(Video)
    case _ => None
  }
}