package com.coded.bankingproject.domain.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "accounts")
data class AccountEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    val user: UserEntity? = null,

    @Column(name="balance")
    val balance: BigDecimal,

    @Column(name="is_active")
    val isActive: Boolean,

    @Column(name="account_number", unique = true)
    val accountNumber: String,


) {
    constructor(): this(null, null, BigDecimal.ZERO, false, "")
}