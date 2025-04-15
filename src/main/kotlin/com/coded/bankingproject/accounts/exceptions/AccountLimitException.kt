package com.coded.bankingproject.accounts.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class AccountLimitException(
    override val message: String = "Account limit reached. Please visit your nearest NBK branch."
) : RuntimeException(message)