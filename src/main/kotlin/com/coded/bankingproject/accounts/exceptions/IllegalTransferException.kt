package com.coded.bankingproject.accounts.exceptions

import com.coded.bankingproject.enums.AccountErrorCode
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class IllegalTransferException(
    override val message: String = "Cannot transfer to the same account.",
    override val code: AccountErrorCode = AccountErrorCode.INVALID_TRANSFER
) : AccountExceptionBase(message, code)