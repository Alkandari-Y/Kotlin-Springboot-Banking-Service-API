package com.coded.bankingproject.accounts.dtos

import com.coded.bankingproject.domain.entities.AccountEntity
import com.coded.bankingproject.domain.entities.UserEntity
import java.math.BigDecimal

data class AccountCreateRequestDto(
    val userId: Long,
    val initialBalance: BigDecimal,
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