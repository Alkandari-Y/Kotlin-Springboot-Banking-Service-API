package com.coded.bankingproject.accounts.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class AccountNotFoundException(
    override val message: String = "Account not found"
) : RuntimeException(message)