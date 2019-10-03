package com.canal.populator

object Config {
  val folderLocation: String = sys.env.get("TSV_FILES_LOCATION").orElse(sys.env.get("PWD")).get
  val insertionChunkSize: Int = 50000
  val insertionParallelism: Int = 1

  val nameBasics: String = "name.basics.tsv"
  val titleBasics: String = "title.basics.tsv"
  val titlePrincipals: String = "title.principals.tsv"
  val titleEpisodes: String = "title.episodes.tsv"
  val titleRatings: String = "title.ratings.tsv"
}
