package com.coded.bankingproject.services

import com.coded.bankingproject.accounts.dtos.TransactionResultDto
import com.coded.bankingproject.accounts.dtos.TransferCreateRequestDto
import com.coded.bankingproject.domain.entities.UserEntity

interface TransactionService {
    fun transfer(newTransaction: TransferCreateRequestDto, userMakingTransfer: UserEntity): TransactionResultDto
}