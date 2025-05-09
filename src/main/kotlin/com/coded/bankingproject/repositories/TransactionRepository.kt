package com.coded.bankingproject.repositories

import com.coded.bankingproject.domain.entities.TransactionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TransactionRepository: JpaRepository<TransactionEntity, Long> {
    fun findBySourceAccountId(accountId: Long): List<TransactionEntity>
}