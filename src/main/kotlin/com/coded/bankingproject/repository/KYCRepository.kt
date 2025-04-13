package com.coded.bankingproject.repository

import com.coded.bankingproject.domain.entities.KYCEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface KYCRepository: JpaRepository<KYCEntity, Long> {
    fun findKYCByUserId(userId: Long): KYCEntity?
}