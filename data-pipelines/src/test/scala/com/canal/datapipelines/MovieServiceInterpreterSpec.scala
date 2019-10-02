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

  describe("tvSeriesWithGreatestNumberOfEpisodes") {
    it("Should return the 5 titles with greatest numbers of episodes") {
      service
        .tvSeriesWithGreatestNumberOfEpisodes(5)
        .runWith(Sink.seq)
        .map(titles => titles.map(_.primaryTitle).toSet)
        .map(names => {
          names shouldBe Set(
            "Neighbours",
            "Coronation Street",
            "Ohayou Tokushima",
            "Days of Our Lives",
            "The Price Is Right"
          )
        })
    }
  }

  describe("Title by id") {
    it("Should return 'He walked by night' for 'tt0040427'") {
      service
        .titleById("tt0040427")
        .runWith(Sink.last)
        .map(_.primaryTitle shouldBe "He Walked by Night")
    }

    it("Should return 'The Price Is Right' for 'tt0068120'") {
      service
        .titleById("tt0068120")
        .runWith(Sink.last)
        .map(_.primaryTitle shouldBe "The Price Is Right")
    }
  }
}
