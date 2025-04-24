package com.coded.bankingproject.users

import com.coded.bankingproject.auth.dtos.JwtResponseDto
import com.coded.bankingproject.services.JwtService
import com.coded.bankingproject.services.KYCService
import com.coded.bankingproject.services.UserService
import com.coded.bankingproject.users.dtos.*
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users/v1")
class UserController(
    private val userService: UserService,
    private val kycService: KYCService,
    private val authenticationManager: AuthenticationProvider,
    private val userDetailsService: UserDetailsService,
    private val jwtService: JwtService,
    private val passwordEncoder: PasswordEncoder,
) {
    @PostMapping(path=["/register"])
    fun register(@Valid @RequestBody user: RegisterCreateRequestDto
    ): ResponseEntity<JwtResponseDto> {
        val userEntity = userService.createUser(
            user.copy(
                password = passwordEncoder.encode(
                    user.password
                )
            ).toEntity()
        )
        val token = jwtService.generateToken(userEntity.username)
        return ResponseEntity(JwtResponseDto(token), HttpStatus.OK)
    }

    @PostMapping(path = ["/login"])
    fun login(@Valid @RequestBody loginRequestDto: LoginRequestDto): ResponseEntity<*> {
        val authToken = UsernamePasswordAuthenticationToken(
            loginRequestDto.username,
            loginRequestDto.password
        )
        val authenticated = authenticationManager.authenticate(authToken)

        if (authenticated.isAuthenticated.not()) {
            throw UsernameNotFoundException("Invalid credentials")
        }

        val userDetails = userDetailsService.loadUserByUsername(loginRequestDto.username)
        val token = jwtService.generateToken(userDetails.username)
        return ResponseEntity(JwtResponseDto(token), HttpStatus.OK)
    }

    @PostMapping(path=["/kyc"])
    fun updateKYC(
        @Valid @RequestBody kycCreateRequestDto: KYCCreateRequestDto,
            authentication: Authentication,
    ): ResponseEntity<KYCCreateRequestDto> {
        return try {

            // change me later
            // make this implementation cleaner
            // multiple calls for user
            // modify the UserDetails to have the id
            val userDetails = authentication.principal as UserDetails
            val userEntity = userService.findUserById(kycCreateRequestDto.userId)
            kycService.createKYCOrUpdate(kycCreateRequestDto)
            ResponseEntity(kycCreateRequestDto, HttpStatus.CREATED)
        }  catch (e: Exception) {
            ResponseEntity(null, HttpStatus.BAD_REQUEST)
        }
    }

    @GetMapping(path=["/kyc"])
    fun getKYCByUserId(
//        @PathVariable("userId") userId: Long
        authentication: Authentication,
        ): ResponseEntity<KYCResponseDto> {
        val userDetails = authentication.principal as UserDetails
        val userId = userService.findUserByUsername(userDetails.username)?.id
            ?: return ResponseEntity(HttpStatus.NOT_FOUND)

        val kyc = kycService.findKYCByUserId(userId)
            ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        return ResponseEntity(kyc, HttpStatus.OK)
    }
}


