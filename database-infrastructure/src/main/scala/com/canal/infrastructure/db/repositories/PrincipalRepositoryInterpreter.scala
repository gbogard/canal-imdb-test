package com.canal.infrastructure.db.repositories

import cats.effect.Sync
import cats.implicits._
import com.canal.domain._
import com.canal.domain.repositories.PrincipalRepository
import doobie._
import doobie.implicits._

class PrincipalRepositoryInterpreter[F[_]: Sync]()(implicit xa: Transactor[F])
    extends PrincipalRepository[F] {
  override def insertPrincipals(principals: Seq[Principal]): F[Unit] =
    principals.toList.traverse(insertSinglePrincipalQuery).transact(xa).map(_ => ())

  private def insertSinglePrincipalQuery(principal: Principal) =
    sql"""
    INSERT OR IGNORE INTO principals VALUES(
      ${principal.titleId},
      ${principal.ordering},
      ${principal.personId},
      ${principal.category},
      ${principal.job},
      ${principal.characters}
    )
    """.update.run
}
