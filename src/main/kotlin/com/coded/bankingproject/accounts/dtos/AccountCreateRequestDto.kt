package com.coded.bankingproject.accounts.dtos

import com.coded.bankingproject.domain.entities.AccountEntity
import com.coded.bankingproject.domain.entities.UserEntity
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import java.math.BigDecimal

data class AccountCreateRequestDto(
    @field:NotBlank
    val userId: Long,
    @field:NotBlank
    @field:DecimalMin(value = "1.00", inclusive = true, message = "Initial deposit minimum 100.00")
    val initialBalance: BigDecimal,
    @field:NotBlank
    val name: String,
)

fun AccountCreateRequestDto.toEntity(user: UserEntity): AccountEntity {
    return AccountEntity(
        name=name,
        user = user,
        balance = initialBalance,
        isActive = true
    )
}