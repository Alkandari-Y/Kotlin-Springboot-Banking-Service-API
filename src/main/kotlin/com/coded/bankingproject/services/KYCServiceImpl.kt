package com.coded.bankingproject.services

import com.coded.bankingproject.accounts.exceptions.AccountVerificationException
import com.coded.bankingproject.domain.entities.KYCEntity
import com.coded.bankingproject.domain.entities.UserEntity
import com.coded.bankingproject.errors.ErrorCode
import com.coded.bankingproject.repositories.KYCRepository
import com.coded.bankingproject.repositories.kycDateFormatter
import com.coded.bankingproject.users.dtos.KYCCreateRequestDto
import com.coded.bankingproject.users.dtos.KYCResponseDto
import com.coded.bankingproject.users.dtos.toEntity
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.Period

@Service
class KYCServiceImpl(
    private val kycRepository: KYCRepository,
): KYCService {

    override fun createKYCOrUpdate(
        kycRequest: KYCCreateRequestDto,
        user: UserEntity
    ): KYCEntity {
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
        val yearsOfAge = Period.between(newKycEntity.dateOfBirth, currentDate).years
        if (yearsOfAge < 18) throw AccountVerificationException(message = "User must be 18 or older", code = ErrorCode.INVALID_AGE)

        return kycRepository.save(newKycEntity)
    }

    override fun findKYCByUserId(userId: Long): KYCResponseDto? {
        return kycRepository.findKYCByUserId(userId)
    }
}