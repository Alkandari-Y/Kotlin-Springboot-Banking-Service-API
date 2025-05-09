package com.coded.bankingproject.users.dtos

import com.coded.bankingproject.domain.entities.KYCEntity
import com.coded.bankingproject.domain.entities.UserEntity
import com.coded.bankingproject.repositories.kycDateFormatter
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import org.jetbrains.annotations.NotNull
import org.springframework.format.annotation.DateTimeFormat
import java.math.BigDecimal
import java.time.LocalDate

data class KYCCreateRequestDto(
    @field:NotBlank
    val firstName: String,
    @field:NotBlank
    val lastName: String,
    @field:NotBlank
    @field:DateTimeFormat(pattern = "dd-MM-yyyy")
    val dateOfBirth: String,
    @field:NotNull
    @field:DecimalMin(value = "0.00", inclusive = true, message = "Minimum salary cannot be below 0")
    val salary: BigDecimal,
    @field:NotBlank
    val nationality: String,
)

fun KYCCreateRequestDto.toEntity(user: UserEntity) = KYCEntity(
    user = user,
    firstName = firstName,
    lastName = lastName,
    dateOfBirth = LocalDate.parse(dateOfBirth, kycDateFormatter),
    salary = salary,
    nationality=nationality
)

