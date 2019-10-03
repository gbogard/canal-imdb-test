package com.canal.populator

import cats.effect._
import cats.implicits._
import doobie._
import doobie.implicits._

object Schema {
  private val schema = List(
    sql"""
    CREATE TABLE IF NOT EXISTS titles(
      id VARCHAR(12) NOT NULL,
      title_type VARCHAR(20) NOT NULL,
      primary_title VARCHAR(256) NOT NULL,
      original_title VARCHAR(256) NOT NULL,
      is_adult BOOLEAN NOT NULL,
      start_year TINYINT,
      end_year TINYINT,
      runtime_minutes INT,
      genres TEXT,
      PRIMARY KEY (id)
    )
    """,
    sql"""
    CREATE TABLE IF NOT EXISTS people(
      id VARCHAR(12) NOT NULL,
      primary_name VARCHAR(256) NOT NULL,
      birth_year TINYINT,
      death_year TINYINT,
      primary_profession TEXT,
      PRIMARY KEY (id)
    )
    """,
    sql"""
    CREATE TABLE IF NOT EXISTS principals(
      title_id VARCHAR(12) NOT NULL,
      ordering TINYINT,
      person_id VARCHAR(12) NOT NULL,
      category VARCHAR(50) NOT NULL,
      job VARCHAR(256),
      characters VARCHAR(256) NOT NULL,
      FOREIGN KEY(title_id) REFERENCES titles(id),
      FOREIGN KEY(person_id) REFERENCES people(id),
      PRIMARY KEY (title_id, person_id)
    )
    """,
    sql"""
    CREATE TABLE IF NOT EXISTS episodes(
      title_id VARCHAR(12) NOT NULL,
      parent_title_id VARCHAR(12) NOT NULL,
      season_number INT,
      episode_number INT,
      FOREIGN KEY(title_id) REFERENCES titles(id),
      FOREIGN KEY(parent_title_id) REFERENCES titles(id),
      PRIMARY KEY (title_id)
    )
    """,
    sql"""
    CREATE INDEX IF NOT EXISTS titles_primary_title_index ON titles(primary_title)
    """,
    sql"""
    CREATE INDEX IF NOT EXISTS titles_original_title_index ON titles(original_title)
    """,
    sql"""
    CREATE INDEX IF NOT EXISTS episode_parent_id_index ON episodes(parent_title_id)
    """
  )


  def migrateDatabase(xa: Transactor[IO]): IO[Unit] =
    schema.map(_.update.run).sequence.transact(xa).map(_ => ())
}
