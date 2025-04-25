package com.coded.bankingproject.services

import com.coded.bankingproject.domain.entities.UserEntity
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtService {
    private val secretKey: SecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256)
    private val expirationMs: Long = 1000 * 60 * 60

    fun generateToken(user: UserEntity): String {
        val now = Date()
        val expiry = Date(now.time + expirationMs)

        return Jwts.builder()
            .setSubject(user.username)
            .claim("userId", user.id)
            .claim("role", user.role.name)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(secretKey)
            .compact()
    }

    fun extractUsername(token: String): String =
        parseToken(token).subject

    fun extractUserId(token: String): Long =
        parseToken(token).get("userId", Integer::class.java).toLong()

    fun extractRole(token: String): String =
        parseToken(token).get("role", String::class.java)

    fun isTokenValid(token: String, username: String): Boolean =
        extractUsername(token) == username

    private fun parseToken(token: String): Claims =
        Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
}