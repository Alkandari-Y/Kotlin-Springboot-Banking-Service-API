package com.coded.bankingproject.services

import com.coded.bankingproject.domain.entities.KYCEntity
import com.coded.bankingproject.users.dtos.KYCCreateRequestDto

interface KYCService {
    fun createKYC(kycRequest: KYCCreateRequestDto): KYCEntity
}