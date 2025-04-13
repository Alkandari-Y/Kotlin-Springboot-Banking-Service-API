package com.coded.bankingproject.repository

import com.coded.bankingproject.domain.entities.AccountEntity
import com.coded.bankingproject.domain.projections.AccountListItemProjection
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface AccountRepository: JpaRepository<AccountEntity, Long> {
    @Query("SELECT a FROM AccountEntity a")
    fun allAccounts(): List<AccountListItemProjection>
}

// name entity graphy n + 1 entity queries