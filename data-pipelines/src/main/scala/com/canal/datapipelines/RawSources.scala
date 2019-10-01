package com.canal.datapipelines

import com.canal.domain._
import akka.NotUsed
import akka.stream.IOResult
import akka.stream.alpakka.csv.scaladsl._
import akka.stream.scaladsl._
import akka.util.ByteString
import java.nio.file.Paths
import scala.concurrent.Future

object RawSources {

  val titlesSource: Source[Title, _] =
    readFile(Files.titleBasics).via(parse(Parsers.mapToTitle))
  val peopleSource: Source[Person, _] =
    readFile(Files.nameBasics).via(parse(Parsers.mapToPerson))
  val episodesSource: Source[Episode, _] =
    readFile(Files.titleEpisodes).via(parse(Parsers.mapToEpisode))
  val principalsSource: Source[Principal, _] =
    readFile(Files.titlePrincipals).via(parse(Parsers.mapToPincipal))
  val ratingsSource: Source[Rating, _] =
    readFile(Files.titleRatings).via(parse(Parsers.mapToRating))

  private lazy val csvParser: Flow[ByteString, List[ByteString], NotUsed] =
    CsvParsing.lineScanner(delimiter = CsvParsing.Tab, quoteChar = CsvParsing.Tab)

  private def readFile(fileName: String): Source[Map[String, String], Future[IOResult]] = {
    val path = Paths.get(Files.folderLocation, fileName)
    FileIO.fromPath(path)
      .via(csvParser)
      .via(CsvToMap.toMapAsStrings())
  }

  private def parse[T](parser: Map[String, String] => Option[T]): Flow[Map[String, String], T, NotUsed] =
    Flow[Map[String, String]].map(parser).collect({
      case Some(value) => value
    })
}
