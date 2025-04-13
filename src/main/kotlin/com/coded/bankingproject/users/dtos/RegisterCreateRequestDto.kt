package com.coded.bankingproject.users.dtos

import com.coded.bankingproject.domain.entities.UserEntity
import jakarta.validation.constraints.*

data class RegisterCreateRequestDto(
    @field:NotBlank(message = "Username is required")
    val username: String,
    @field:NotBlank(message = "Password is required")
    val password: String,
)


fun RegisterCreateRequestDto.toEntity() = UserEntity(
    username=username,
    password=password
)