package com.canal.domain

case class PersonWithJob(
  id: String,
  primaryName: String,
  birthYear: Option[Short],
  deathYear: Option[Short],
  primaryProfession: Vector[String],
  category: String,
  job: Option[String],
  characters: Option[String]
)

object PersonWithJob {
  def fromPrincipalAndPerson(person: Person, principal: Principal): PersonWithJob = PersonWithJob(
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