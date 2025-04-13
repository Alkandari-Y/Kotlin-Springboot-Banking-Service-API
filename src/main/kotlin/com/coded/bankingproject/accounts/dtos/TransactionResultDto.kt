package com.coded.bankingproject.accounts.dtos

import com.coded.bankingproject.domain.entities.AccountEntity
import com.coded.bankingproject.domain.entities.TransactionEntity

data class TransactionResultDto(
    val sourceAccount: AccountEntity,
    val destinationAccount: AccountEntity,
    val transaction: TransactionEntity,
)