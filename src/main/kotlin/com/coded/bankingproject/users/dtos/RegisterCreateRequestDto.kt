package com.coded.bankingproject.users.dtos

import com.coded.bankingproject.domain.entities.UserEntity
import jakarta.validation.constraints.*
import org.hibernate.validator.constraints.Length

data class RegisterCreateRequestDto(
    @field:NotBlank(message = "Username is required")
    @field:Length(min = 3, message = "Username is too short")
    val username: String,
    @field:NotBlank(message = "Password is required")
    @field:Length(min = 6, message = "Password is too short")
    @field:Pattern(regexp = """(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).*""", message = "Password is too simple")
    val password: String,
)


fun RegisterCreateRequestDto.toEntity() = UserEntity(
    username=username,
    password=password
)