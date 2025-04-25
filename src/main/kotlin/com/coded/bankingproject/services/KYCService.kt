package com.coded.bankingproject.services

import com.coded.bankingproject.domain.entities.KYCEntity
import com.coded.bankingproject.domain.entities.UserEntity
import com.coded.bankingproject.users.dtos.KYCCreateRequestDto
import com.coded.bankingproject.users.dtos.KYCResponseDto

interface KYCService {
    fun createKYCOrUpdate(kycRequest: KYCCreateRequestDto, user: UserEntity): KYCEntity
    fun findKYCByUserId(userId: Long): KYCResponseDto?
}