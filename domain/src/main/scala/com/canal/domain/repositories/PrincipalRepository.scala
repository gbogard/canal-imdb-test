package com.canal.domain.repositories
import com.canal.domain.Principal

trait PrincipalRepository[F[_]] {
  
  def insertPrincipals(principals: Seq[Principal]): F[Unit]
}