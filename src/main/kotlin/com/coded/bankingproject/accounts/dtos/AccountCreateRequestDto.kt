package com.coded.bankingproject.accounts.dtos

import com.coded.bankingproject.domain.entities.AccountEntity
import com.coded.bankingproject.domain.entities.UserEntity
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import org.jetbrains.annotations.NotNull
import java.math.BigDecimal

data class AccountCreateRequestDto(
    @field:NotNull
    @field:DecimalMin(
        value = "100.00",
        inclusive = true,
        message = "Initial balance must be at least 100.00"
    )
    val initialBalance: BigDecimal,
    @field:NotBlank(message="Account name cannot be blank")
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