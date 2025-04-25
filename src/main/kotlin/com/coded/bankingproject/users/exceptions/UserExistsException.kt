package com.coded.bankingproject.users.exceptions

import com.coded.bankingproject.errors.APIBaseException
import com.coded.bankingproject.errors.ErrorCode
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class UserExistsException (
override val message: String = "User Already Exists",
    override val code: ErrorCode = ErrorCode.USER_ALREADY_EXISTS,
    override val httpStatus: HttpStatus = HttpStatus.BAD_REQUEST,
    override val cause: Throwable? = null
) : APIBaseException(message, httpStatus, code, cause)