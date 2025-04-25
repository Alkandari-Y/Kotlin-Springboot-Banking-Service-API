package com.coded.bankingproject.errors

import org.springframework.http.HttpStatus

open class APIBaseException(
    override val message: String = "Internal Server Error",
    open val httpStatus: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
    open val code: ErrorCode = ErrorCode.INTERNAL_SERVER_ERROR,
    override val cause: Throwable? = null
) : RuntimeException(message, cause)
