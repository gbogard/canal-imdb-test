package com.canal.datapipelines

import org.scalatest._
import com.canal.domain._
import com.canal.datapipelines.Materializer._
import akka.stream.scaladsl._

/**
  * These basic tests ensure the parsers work properly
  */
class RawSourcesSpec extends AsyncFunSpec with Matchers {

  describe("Titles") {
    it("should have Carmencita as the first element") {
      val expectedTitle = Title(
        "tt0000001",
        TitleType.Short,
        "Carmencita",
        "Carmencita",
        false,
        Some(1894),
        None,
        Some(1),
        Vector("Documentary", "Short")
      )

      RawSources.titlesSource
        .take(1)
        .runWith(Sink.last)
        .map(_ shouldBe expectedTitle)
    }
  }

  describe("People") {
    it("Should have Fred Astaire as the first person") {
      val expectedPerson = Person(
        "nm0000001",
        "Fred Astaire",
        Some(1899),
        Some(1987),
        Vector("soundtrack", "actor", "miscellaneous"),
        Vector("tt0053137", "tt0050419", "tt0072308", "tt0043044")
      )

      RawSources.peopleSource
        .take(1)
        .runWith(Sink.last)
        .map(_ shouldBe expectedPerson)
    }
  }

  describe("Episodes") {
    it("Should have tt0041951 as first episode") {
      val expectedEpisode = Episode(
        "tt0041951",
        "tt0041038",
        1,
        9
      )
      RawSources.episodesSource
        .take(1)
        .runWith(Sink.last)
        .map(_ shouldBe expectedEpisode)
    }
  }

  describe("Principals") {
    it("Should have principals for tt0000001 as first element") {
      val expectedPrincipal = Principal(
        "tt0000001",
        1,
        "nm1588970",
        "self",
        None,
        Some("[\"Herself\"]")
      )

      RawSources.principalsSource
        .take(1)
        .runWith(Sink.last)
        .map(_ shouldBe expectedPrincipal)
    }
  }

  describe("Ratings") {
    it("Should have ratings for title tt0000001 as first element") {
      val expectedRating = Rating("tt0000001", 5.6f, 1538)

      RawSources.ratingsSource
        .take(1)
        .runWith(Sink.last)
        .map(_ shouldBe expectedRating)

    }
  }
}
