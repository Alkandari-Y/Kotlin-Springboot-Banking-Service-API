package com.coded.bankingproject.services


import com.coded.bankingproject.domain.entities.AuthUserDetails
import com.coded.bankingproject.repositories.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AuthUserDetailsService (
    private val usersRepository: UserRepository,
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = usersRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User not found")
        val authorities = listOf(SimpleGrantedAuthority("ROLE_${user.role.name}"))

        return AuthUserDetails(
            userId = user.id!!,
            username = user.username,
            password = user.password,
            authorities = authorities
        )
    }
}