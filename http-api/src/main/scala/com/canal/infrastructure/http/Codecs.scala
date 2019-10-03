package com.canal.infrastructure.http

import com.canal.domain.{PersonWithJob, TitleType, TitleWithEpisodeCount}
import io.circe.Encoder
import io.circe.generic.semiauto._

object Codecs {
  implicit val titleTypeEncoder: Encoder[TitleType] = Encoder.encodeString.contramap(_.id)

  implicit val personWithJobEncoder: Encoder[PersonWithJob] = deriveEncoder
  implicit val titleWithEpisodeCountEncoder: Encoder[TitleWithEpisodeCount] = deriveEncoder
}
