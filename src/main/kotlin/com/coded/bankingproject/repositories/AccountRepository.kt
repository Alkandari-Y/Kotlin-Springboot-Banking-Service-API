package com.coded.bankingproject.repositories

import com.coded.bankingproject.domain.entities.AccountEntity
import com.coded.bankingproject.domain.projections.AccountListItemProjection
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository: JpaRepository<AccountEntity, Long> {
    @Query("SELECT a FROM AccountEntity a WHERE a.isActive = TRUE")
    fun allAccounts(): List<AccountListItemProjection>

    fun findByAccountNumber(accountNumber: String): AccountEntity?

    @Query("SELECT COUNT(a) FROM AccountEntity a WHERE a.user.id = :userId")
    fun findAllByUserId(@Param("userId") userId: Long): Int
}