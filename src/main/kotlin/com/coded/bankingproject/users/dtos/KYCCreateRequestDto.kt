package com.coded.bankingproject.users.dtos

import com.coded.bankingproject.domain.entities.KYCEntity
import com.coded.bankingproject.domain.entities.UserEntity
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import org.springframework.format.annotation.DateTimeFormat
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter

var formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

data class KYCCreateRequestDto(
    @field:NotBlank
    @field:Positive
    val userId: Long,
    @field:NotBlank
    val firstName: String,
    @field:NotBlank
    val lastName: String,
    @field:NotBlank
    @field:DateTimeFormat(pattern = "dd-MM-yyyy")
    val dateOfBirth: String,
    @field:NotBlank
    @field:DecimalMin(value = "1.00", inclusive = true, message = "Minimum salary of 100.00")
    val salary: BigDecimal,
    @field:NotBlank
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

