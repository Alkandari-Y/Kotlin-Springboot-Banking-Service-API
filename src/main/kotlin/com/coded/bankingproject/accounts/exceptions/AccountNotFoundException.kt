package com.coded.bankingproject.accounts.exceptions

import com.coded.bankingproject.errors.APIBaseException
import com.coded.bankingproject.errors.ErrorCode
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class AccountNotFoundException(
override val message: String = "Account not found",
override val code: ErrorCode = ErrorCode.ACCOUNT_NOT_FOUND,
override val httpStatus: HttpStatus = HttpStatus.NOT_FOUND,
override val cause: Throwable? = null
) : APIBaseException(message, httpStatus, code, cause)