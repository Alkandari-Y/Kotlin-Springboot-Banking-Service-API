package com.coded.bankingproject.repositories

import com.coded.bankingproject.domain.entities.KYCEntity
import com.coded.bankingproject.users.dtos.KYCResponseDto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface KYCRepository: JpaRepository<KYCEntity, Long> {
    fun findKYCEntityByUserId(userId: Long): KYCEntity?

    @Query("""
    SELECT new com.coded.bankingproject.users.dtos.KYCResponseDto(
        k.user.id, k.firstName, k.lastName, k.dateOfBirth, k.salary
    )
    FROM KYCEntity k
    WHERE k.user.id = :userId
""")
    fun findKYCByUserId(@Param("userId") userId: Long): KYCResponseDto?

}