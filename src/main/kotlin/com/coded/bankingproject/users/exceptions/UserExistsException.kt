package com.coded.bankingproject.users.exceptions

import com.coded.bankingproject.enums.UserErrorCode
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class UserExistsException (
override val message: String = "User Already Exists",
    override val code: UserErrorCode = UserErrorCode.USER_ALREADY_EXISTS
) : UserExceptionBase(message, code)