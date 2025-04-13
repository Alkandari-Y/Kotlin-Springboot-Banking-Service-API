package com.coded.bankingproject.domain.projections

import java.math.BigDecimal

interface AccountListItemProjection {
    val user: UserBaseProjection
    val id: Long
    val balance: BigDecimal
    val isActive: Boolean
    val accountNumber: String
    val name: String
}