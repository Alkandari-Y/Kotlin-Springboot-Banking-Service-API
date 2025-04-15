package com.coded.bankingproject.accounts.exceptions

import com.coded.bankingproject.enums.AccountErrorCode
import com.coded.bankingproject.shared.Exceptions.BaseException

open class AccountExceptionBase (
    override val message: String = "default",
    override val code: AccountErrorCode
) : BaseException(message, code)