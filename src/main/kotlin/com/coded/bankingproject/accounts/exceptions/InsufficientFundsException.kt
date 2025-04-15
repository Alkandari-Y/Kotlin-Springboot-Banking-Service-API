package com.coded.bankingproject.accounts.exceptions

import com.coded.bankingproject.enums.AccountErrorCode
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class InsufficientFundsException(
    override val message: String = "Insufficient balance.",
    override val code: AccountErrorCode = AccountErrorCode.INVALID_TRANSFER
) : AccountExceptionBase(message, code)