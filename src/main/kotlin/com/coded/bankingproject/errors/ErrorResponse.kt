package com.coded.bankingproject.errors

data class ValidationError(
    val field: String,
    val message: String
)

data class ErrorResponse(
    val timestamp: String,
    val status: Int,
    val error: String,
    val message: String,
    val code: String,
    val path: String,
    val fieldErrors: List<ValidationError>? = null
)
