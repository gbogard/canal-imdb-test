package com.canal.domain

case class Principal( 
  titleId: String,
  ordering: Short,
  personId: String,
  category: String,
  job: String,
  characters: Option[String]
)