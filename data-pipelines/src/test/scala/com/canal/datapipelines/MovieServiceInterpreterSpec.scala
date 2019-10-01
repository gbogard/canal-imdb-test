package com.canal.datapipelines

import org.scalatest._
import com.canal.datapipelines.Materializer._
import akka.stream.scaladsl._

class MovieServiceInterpreterSpec extends AsyncFunSpec with Matchers {
  private val service = new MovieServiceInterpreter()

  describe("Principals for movie name") {
    it("Should return cast members for Toy Story") {
      service
        .principalsForMovieName("Toy Story")
        .runWith(Sink.seq)
        .map(_.toSet)
        .map(members => {
          val names = members.map(_.primaryName)
          names shouldBe Set(
            "Don Rickles",
            "Andrew Stanton",
            "John Lasseter",
            "Tim Allen",
            "Tom Hanks",
            "Pete Docter",
            "Joe Ranft",
            "Joel Cohen",
            "Joss Whedon",
            "Jim Varney"
          )
        })
    }

    it("Should return cast members for Mulholland Drive") {
      service
        .principalsForMovieName("Mulholland Dr.")
        .runWith(Sink.seq)
        .map(_.toSet)
        .map(members => {
          val names = members.map(_.primaryName)
          names shouldBe Set(
            "Justin Theroux",
            "Alain Sarde",
            "David Lynch",
            "Neal Edelstein",
            "Jeanne Bates",
            "Laura Harring",
            "Tony Krantz",
            "Mary Sweeney",
            "Michael Polaire",
            "Naomi Watts"
          )
        })
    }
  }
}
