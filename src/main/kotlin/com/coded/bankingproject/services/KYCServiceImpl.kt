package com.coded.bankingproject.services

import com.coded.bankingproject.domain.entities.KYCEntity
import com.coded.bankingproject.repositories.KYCRepository
import com.coded.bankingproject.repositories.UserRepository
import com.coded.bankingproject.repositories.kycDateFormatter
import com.coded.bankingproject.users.dtos.KYCCreateRequestDto
import com.coded.bankingproject.users.dtos.KYCResponseDto
import com.coded.bankingproject.users.dtos.toEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.Period

@Service
class KYCServiceImpl(
    private val kycRepository: KYCRepository,
    private val userRepository: UserRepository
): KYCService {
    override fun createKYCOrUpdate(kycRequest: KYCCreateRequestDto): KYCEntity {
        val user = userRepository.findByIdOrNull(kycRequest.userId)
            ?: throw IllegalArgumentException("User does not exists")

        val existingKYC = kycRepository.findKYCEntityByUserId(user.id!!)

        val newKycEntity  = existingKYC?.copy(
            firstName= kycRequest.firstName,
            lastName= kycRequest.lastName,
            dateOfBirth= kycRequest.dateOfBirth.let {
                LocalDate.parse(kycRequest.dateOfBirth, kycDateFormatter)
            },
            nationality= kycRequest.nationality
        )
            ?: kycRequest.toEntity(user)

        val currentDate = LocalDate.now()
        val yearsOfAge = Period.between(currentDate, newKycEntity.dateOfBirth)
        println("years of age: $yearsOfAge")

        return kycRepository.save(newKycEntity)
    }

    override fun findKYCByUserId(userId: Long): KYCResponseDto? {
        return kycRepository.findKYCByUserId(userId)
    }
}