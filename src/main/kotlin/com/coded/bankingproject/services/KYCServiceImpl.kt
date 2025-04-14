package com.coded.bankingproject.services

import com.coded.bankingproject.domain.entities.KYCEntity
import com.coded.bankingproject.repositories.KYCRepository
import com.coded.bankingproject.repositories.UserRepository
import com.coded.bankingproject.users.dtos.KYCCreateRequestDto
import com.coded.bankingproject.users.dtos.KYCResponseDto
import com.coded.bankingproject.users.dtos.toEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class KYCServiceImpl(
    private val kycRepository: KYCRepository,
    private val userRepository: UserRepository
): KYCService {
    override fun createKYC(kycRequest: KYCCreateRequestDto): KYCEntity {
        val user = userRepository.findByIdOrNull(kycRequest.userId)
            ?: throw IllegalArgumentException("User does not exists")

        kycRepository.findKYCEntityByUserId(user.id!!)?.let {
            throw IllegalArgumentException("KYC Exists")
        }

        return kycRepository.save(kycRequest.toEntity(user))
    }

    override fun findKYCByUserId(userId: Long): KYCResponseDto? {
        return kycRepository.findKYCByUserId(userId)
    }
}