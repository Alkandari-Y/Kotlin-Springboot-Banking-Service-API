package com.coded.bankingproject.domain.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "transactions")
data class TransactionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    val sourceAccount: AccountEntity?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    val destinationAccount: AccountEntity?,

    @Column(name = "amount", nullable = false)
    val amount: BigDecimal,

    ) {
    constructor() : this(null, null, null, BigDecimal.ZERO)
}
