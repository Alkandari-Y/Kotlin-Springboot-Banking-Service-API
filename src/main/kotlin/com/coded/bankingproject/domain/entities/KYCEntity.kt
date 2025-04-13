package com.coded.bankingproject.domain.entities

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate

@Entity
@Table(name = "kycs")
data class KYCEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long? = null,

    @OneToOne
    @JoinColumn(name = "user_id")
    val user: UserEntity?,

    @Column(name="first_name")
    val firstName: String,

    @Column(name="last_name")
    val lastName: String,

    @Column(name = "date_of_birth", nullable = false)
    val dateOfBirth: LocalDate,

    @Column(name="nationality", nullable = false)
    val nationality: String,

    @Column(name = "salary", precision = 9, scale = 2, nullable = false)
    val salary: BigDecimal,
) {
    constructor(): this(
        id =null,
        user=null,
        firstName="",
        lastName="",
        dateOfBirth=LocalDate.now(),
        nationality="",
        salary=BigDecimal(0.0),
    )
}
