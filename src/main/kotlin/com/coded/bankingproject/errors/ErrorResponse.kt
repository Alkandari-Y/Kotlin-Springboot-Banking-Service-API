package com.coded.bankingproject.errors

data class ErrorResponse(
    val timestamp: String,
    val status: Int,
    val error: String,
    val message: String,
    val code: String,
    val path: String
)
