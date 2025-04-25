package com.coded.bankingproject.domain.entities

import jakarta.persistence.*

enum class Role {
    USER, ADMIN
}

@Entity
@Table(name = "users")
data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @Column(name = "username", nullable = false, unique = true)
    val username: String,

    @Column(name = "password", nullable = false, updatable = true)
    val password: String,

    @Column(name = "role", nullable = false)
    val role: Role = Role.USER,
) {
    constructor() : this(null, "", "", Role.USER)
}
