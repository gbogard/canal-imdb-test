package com.canal.infrastructure.db.repositories

import cats.effect.Sync
import cats.implicits._
import com.canal.infrastructure.db._
import com.canal.domain._
import com.canal.domain.repositories.PersonRepository
import doobie._
import doobie.implicits._

class PersonRepositoryInterpreter[F[_]: Sync]()(implicit xa: Transactor[F])
    extends PersonRepository[F] {
  import PersonRepositoryInterpreter._

  def insertPeople(people: Seq[Person]): F[Unit] =
     people.toList.traverse(insertSinglePersonQuery)
      .transact(xa)
      .map(_ => ())

  def principalsForMovieName(movieName: String): F[List[PersonWithJob]] =
    principalsForMovieNameQuery(movieName).to[List].transact(xa)
}

object PersonRepositoryInterpreter {

  def principalsForMovieNameQuery(movieName: String): doobie.Query0[PersonWithJob] =
    sql"""
      SELECT
      people.id,
      people.primary_name,
      people.birth_year,
      people.death_year,
      people.primary_profession,
      principals.category,
      principals.job,
      principals."characters"
    FROM titles t
      INNER JOIN principals ON principals.title_id = t.id
      INNER JOIN people on people.id = principals.person_id
      WHERE (t.original_title = $movieName OR t.primary_title = $movieName) AND t.title_type = 'movie'
    """.query[PersonWithJob]

  def insertSinglePersonQuery(person: Person) =
    sql"""
      INSERT OR IGNORE INTO people VALUES(
        ${person.id},
        ${person.primaryName},
        ${person.birthYear},
        ${person.deathYear},
        ${person.primaryProfession}
      )
      """.update.run
}
