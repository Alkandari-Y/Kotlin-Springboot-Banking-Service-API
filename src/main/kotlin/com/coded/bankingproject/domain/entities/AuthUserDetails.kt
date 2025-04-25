package com.coded.bankingproject.domain.entities

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails


data class AuthUserDetails(
    val userId: Long,
    private val username: String,
    private val password: String,
    private val authorities: Collection<GrantedAuthority>
) : UserDetails {
    override fun getUsername() = username
    override fun getPassword() = password
    override fun getAuthorities() = authorities
    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true
}

fun AuthUserDetails.toUserEntity(): UserEntity {
    val role = this.authorities
        .map { it.authority }
        .firstOrNull()
        ?.let { Role.valueOf(it.removePrefix("ROLE_")) } ?: Role.USER

    return UserEntity(
        id = this.userId,
        username = this.username,
        password = this.password,
        role = role
    )
}