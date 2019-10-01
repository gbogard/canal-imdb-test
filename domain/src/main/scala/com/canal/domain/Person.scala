package com.canal.domain

case class Person(
  id: String,
  primaryName: String,
  birthYear: Short,
  deathYear: Option[Short],
  primaryProfession: Vector[String],
  titlesIds: Vector[String]
)