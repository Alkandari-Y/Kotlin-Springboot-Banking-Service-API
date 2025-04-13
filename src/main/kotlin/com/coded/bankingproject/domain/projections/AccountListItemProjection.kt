package com.coded.bankingproject.domain.projections

import java.math.BigDecimal

interface AccountListItemProjection {
    val user: UserBaseProjection
    val id: Long
    val balance: BigDecimal
    val is_active: Boolean
    val account_number: String
}