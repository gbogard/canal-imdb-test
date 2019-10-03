package com.canal.infrastructure

import com.canal.domain.TitleType
import doobie.util.{Get, Put}

package object db {
  private val separator = ","
  implicit val stringSeqPut: Put[Vector[String]] = Put[String].contramap(_.mkString(separator))
  implicit val stringSeqGet: Get[Vector[String]] = Get[String].map(_.split(separator).toVector)

  implicit val titleTypePut: Put[TitleType] = Put[String].contramap(_.id)
  implicit val titleTypeGet: Get[TitleType] = Get[String].map(TitleType(_).get)
}
