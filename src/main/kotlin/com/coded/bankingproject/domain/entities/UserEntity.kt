package com.coded.bankingproject.domain.entities

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*

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
//
//    @OneToOne(mappedBy = "user")
//    val kyc: KYCEntity? = null

//    @OneToMany(mappedBy = "user", cascade = [CascadeType.DETACH], fetch = FetchType.LAZY)
//    @JsonManagedReference
//    val accounts: List<AccountEntity> = listOf(),
) {
    constructor() : this(null, "", "")
}
