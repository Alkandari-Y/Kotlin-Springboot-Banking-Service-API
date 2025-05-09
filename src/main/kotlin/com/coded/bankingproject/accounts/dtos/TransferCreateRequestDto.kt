package com.coded.bankingproject.accounts.dtos

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import org.jetbrains.annotations.NotNull
import java.math.BigDecimal

data class TransferCreateRequestDto(
    @field:NotBlank
    val sourceAccountNumber: String,
    @field:NotBlank
    val destinationAccountNumber: String,
    @field:NotNull
    @field:DecimalMin(
        value = "1.00",
        inclusive = true,
        message = "amount must must be at least 1.00"
    )
    val amount: BigDecimal,
)
