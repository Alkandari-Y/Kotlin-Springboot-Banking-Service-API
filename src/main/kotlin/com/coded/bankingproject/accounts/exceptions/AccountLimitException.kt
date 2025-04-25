package com.coded.bankingproject.accounts.exceptions

import com.coded.bankingproject.errors.APIBaseException
import com.coded.bankingproject.errors.ErrorCode
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class AccountLimitException(
    override val message: String = "Account limit reached. Please visit your nearest NBK branch.",
    override val code: ErrorCode = ErrorCode.ACCOUNT_LIMIT_REACHED,
    override val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST,
    override val cause: Throwable? = null
) : APIBaseException(message, httpStatus, code, cause)
