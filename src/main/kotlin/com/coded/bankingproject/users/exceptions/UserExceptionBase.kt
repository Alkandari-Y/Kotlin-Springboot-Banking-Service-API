package com.coded.bankingproject.users.exceptions

import com.coded.bankingproject.enums.UserErrorCode
import com.coded.bankingproject.shared.Exceptions.BaseException

open class UserExceptionBase (
    override val message: String = "User Error",
    override val code: UserErrorCode
) : BaseException(message, code)