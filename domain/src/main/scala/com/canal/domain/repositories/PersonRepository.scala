package com.canal.domain.repositories
import com.canal.domain.{Person, PersonWithJob}

trait PersonRepository[F[_]] {
  
  def insertPeople(people: Seq[Person]): F[Unit]

  def principalsForMovieName(movieName: String): F[List[PersonWithJob]]

}