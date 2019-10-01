package com.canal.domain

case class PersonWithJob(
  id: String,
  primaryName: String,
  birthYear: Short,
  deathYear: Option[Short],
  primaryProfession: Vector[String],
  category: String,
  job: Option[String],
  characters: Option[String]
)

object PersonWithJob {
  def apply(principal: Principal, person: Person): PersonWithJob = PersonWithJob(
    person.id,
    person.primaryName,
    person.birthYear,
    person.deathYear,
    person.primaryProfession,
    principal.category,
    principal.job,
    principal.characters
  )
}