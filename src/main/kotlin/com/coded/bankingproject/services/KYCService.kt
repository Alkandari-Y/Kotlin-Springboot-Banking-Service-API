package com.coded.bankingproject.services

import com.coded.bankingproject.domain.entities.KYCEntity
import com.coded.bankingproject.users.dtos.KYCCreateRequestDto
import com.coded.bankingproject.users.dtos.KYCResponseDto

interface KYCService {
    fun createKYC(kycRequest: KYCCreateRequestDto): KYCEntity
    fun findKYCByUserId(userId: Long): KYCResponseDto?
}