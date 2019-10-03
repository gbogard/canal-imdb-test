package com.canal.populator.data

import java.nio.file.Paths

import akka.NotUsed
import akka.stream.IOResult
import akka.stream.alpakka.csv.scaladsl._
import akka.stream.scaladsl._
import akka.util.ByteString
import com.canal.domain._
import com.canal.populator.Config

import scala.concurrent.Future

object RawSources {

  val titlesSource: Source[Title, _] =
    readFile(Config.titleBasics).via(parse(Parsers.mapToTitle))
  val peopleSource: Source[Person, _] =
    readFile(Config.nameBasics).via(parse(Parsers.mapToPerson))
  val episodesSource: Source[Episode, _] =
    readFile(Config.titleEpisodes).via(parse(Parsers.mapToEpisode))
  val principalsSource: Source[Principal, _] =
    readFile(Config.titlePrincipals).via(parse(Parsers.mapToPincipal))
  val ratingsSource: Source[Rating, _] =
    readFile(Config.titleRatings).via(parse(Parsers.mapToRating))

  private lazy val quoteChar = 0.toByte
  private lazy val csvParser: Flow[ByteString, List[ByteString], NotUsed] =
    CsvParsing.lineScanner(delimiter = CsvParsing.Tab, quoteChar = quoteChar)

  private def readFile(fileName: String): Source[Map[String, String], Future[IOResult]] = {
    val path = Paths.get(Config.folderLocation, fileName)
    FileIO.fromPath(path)
      .via(csvParser)
      .via(CsvToMap.toMapAsStrings())
  }

  private def parse[T](parser: Map[String, String] => Option[T]): Flow[Map[String, String], T, NotUsed] =
    Flow[Map[String, String]].map(parser).collect({
      case Some(value) => value
    })
}
