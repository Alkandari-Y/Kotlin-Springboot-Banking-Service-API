package com.coded.bankingproject.accounts.dtos

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import java.math.BigDecimal

data class TransferCreateRequestDto(
    @field:NotBlank
    val sourceAccountNumber: String,
    @field:NotBlank
    val destinationAccountNumber: String,
    @field:NotBlank
    @field:DecimalMin(value = "1.00", inclusive = true, message = "Amount must be at least 1.00")
    val amount: BigDecimal,
)
