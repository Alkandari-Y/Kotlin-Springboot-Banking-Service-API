package com.coded.bankingproject.accounts.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class IllegalTransferException(
    override val message: String = "Cannot transfer to the same account."
) : RuntimeException(message)