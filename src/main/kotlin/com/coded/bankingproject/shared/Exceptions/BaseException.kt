package com.coded.bankingproject.shared.Exceptions

open class BaseException (
    override val message: String = "default",
    open val code: Enum<*>
) : RuntimeException(message)