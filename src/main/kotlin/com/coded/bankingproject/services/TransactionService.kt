package com.coded.bankingproject.services

import com.coded.bankingproject.accounts.dtos.TransactionResultDto
import com.coded.bankingproject.accounts.dtos.TransferCreateRequestDto

interface TransactionService {
    fun transfer(newTransaction: TransferCreateRequestDto): TransactionResultDto
}