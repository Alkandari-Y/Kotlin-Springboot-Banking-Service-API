package com.coded.bankingproject.users.dtos

import java.math.BigDecimal
import java.time.LocalDate

data class KYCResponseDto(
    val userId: Long,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: LocalDate,
    val salary: BigDecimal
)
