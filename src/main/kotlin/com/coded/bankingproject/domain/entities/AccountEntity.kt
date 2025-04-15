package com.coded.bankingproject.domain.entities


import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import java.math.BigDecimal
import java.util.UUID

@Entity
@Table(name = "accounts")
data class AccountEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @Column(name = "name")
    val name: String,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    val user: UserEntity? = null,

    @Column(name="balance")
    val balance: BigDecimal,

    @Column(name="is_active")
    val isActive: Boolean,

    @Column(name="account_number", unique = true)
    val accountNumber: String = UUID.randomUUID().toString()
        .replace("[A-Za-z]".toRegex(), "")
        .replace("-", ""),


) {
    constructor(): this(null, "", null, BigDecimal.ZERO, false, "")
}