package com.coded.bankingproject.accounts.exceptions

import com.coded.bankingproject.enums.ErrorCode
import com.coded.bankingproject.shared.Exceptions.BaseException

open class AccountExceptionBase (
    override val message: String = "default",
    override val code: ErrorCode = ErrorCode.ACCOUNT_LIMIT_REACHED,
) : BaseException(message, code)