package com.coded.bankingproject.accounts.exceptions

import com.coded.bankingproject.enums.AccountErrorCode
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class AccountNotFoundException(
    override val message: String = "Account not found",
    override val code: AccountErrorCode = AccountErrorCode.ACCOUNT_NOT_FOUND
) : AccountExceptionBase(message, code)