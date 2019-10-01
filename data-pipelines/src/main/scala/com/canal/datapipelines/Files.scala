package com.canal.datapipelines

object Files {

  val folderLocation: String = sys.env.get("TSV_FILES_LOCATION").orElse(sys.env.get("PWD")).get

  val nameBasics: String = "name.basics.tsv"
  val titleBasics: String = "title.basics.tsv" 
  val titlePrincipals: String = "title.principals.tsv"
  val titleEpisodes: String = "title.episodes.tsv"
  val titleRatings: String = "title.ratings.tsv"
}