package com.coded.bankingproject.accounts.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class InsufficientFundsException(
    override val message: String = "Insufficient balance."
) : RuntimeException(message)