package com.coded.bankingproject.users.dtos

import com.coded.bankingproject.domain.entities.KYCEntity
import com.coded.bankingproject.domain.entities.UserEntity
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DecimalStyle

var formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

data class KYCCreateRequestDto(
    val userId: Long,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: String,
    val salary: BigDecimal,
    val nationality: String,
)

fun KYCCreateRequestDto.toEntity(user: UserEntity) = KYCEntity(
    user = user,
    firstName = firstName,
    lastName = lastName,
    dateOfBirth = LocalDate.parse(dateOfBirth, formatter),
    salary = salary,
    nationality=nationality
)

