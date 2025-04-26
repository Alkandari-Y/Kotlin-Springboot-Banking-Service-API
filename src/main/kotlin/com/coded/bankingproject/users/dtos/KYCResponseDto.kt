package com.coded.bankingproject.users.dtos

import com.coded.bankingproject.domain.entities.KYCEntity
import com.coded.bankingproject.domain.entities.UserEntity
import com.fasterxml.jackson.annotation.JsonFormat
import java.math.BigDecimal
import java.time.LocalDate

data class KYCResponseDto(
    val id: Long,
    val userId: Long,
    val firstName: String,
    val lastName: String,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    var dateOfBirth: LocalDate? = null,
    val salary: BigDecimal,
    val nationality: String
)


fun KYCEntity.toKYCResponseDto(userId: Long) = KYCResponseDto(
    id = id!!,
    userId = userId,
    firstName = firstName,
    lastName = lastName,
    dateOfBirth = dateOfBirth,
    salary = salary,
    nationality = nationality
)