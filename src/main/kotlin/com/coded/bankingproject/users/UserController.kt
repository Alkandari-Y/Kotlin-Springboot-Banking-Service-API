package com.coded.bankingproject.users

import com.coded.bankingproject.auth.dtos.JwtResponseDto
import com.coded.bankingproject.domain.entities.AuthUserDetails
import com.coded.bankingproject.domain.entities.toUserEntity
import com.coded.bankingproject.services.JwtService
import com.coded.bankingproject.services.KYCService
import com.coded.bankingproject.services.UserService
import com.coded.bankingproject.users.dtos.*
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users/v1")
class UserController(
    private val userService: UserService,
    private val kycService: KYCService,
    private val authenticationManager: AuthenticationProvider,
    private val jwtService: JwtService,
    private val passwordEncoder: PasswordEncoder,
) {
    @PostMapping(path = ["/register"])
    fun register(
        @Valid @RequestBody user: RegisterCreateRequestDto
    ): ResponseEntity<JwtResponseDto> {
        val userEntity = userService.createUser(
            user.copy(
                password = passwordEncoder.encode(
                    user.password
                )
            ).toEntity()
        )
        val token = jwtService.generateToken(userEntity)
        return ResponseEntity(JwtResponseDto(token), HttpStatus.OK)
    }

    @PostMapping(path = ["/login"])
    fun login(
        @Valid @RequestBody loginRequestDto: LoginRequestDto
    ): ResponseEntity<JwtResponseDto> {
        val authToken = UsernamePasswordAuthenticationToken(
            loginRequestDto.username,
            loginRequestDto.password
        )

        val authentication = authenticationManager.authenticate(authToken)
        val userDetails = authentication.principal as? AuthUserDetails
            ?: throw UsernameNotFoundException("User not found")

        val token = jwtService.generateToken(userDetails.toUserEntity())
        return ResponseEntity(JwtResponseDto(token), HttpStatus.OK)
    }

    @PostMapping(path = ["/kyc"])
    fun updateKYC(
        @Valid @RequestBody kycCreateRequestDto: KYCCreateRequestDto,
        @AuthenticationPrincipal user: AuthUserDetails
    ): ResponseEntity<KYCResponseDto> {
        val authUser = user.toUserEntity()
        val kyc = kycService.createKYCOrUpdate(
            kycCreateRequestDto,
            authUser
        )
        return ResponseEntity(kyc.toKYCResponseDto(authUser.id!!), HttpStatus.CREATED)
    }

    @GetMapping(path = ["/kyc"])
    fun getKYCByUserId(
        @AuthenticationPrincipal user: AuthUserDetails
    ): ResponseEntity<KYCResponseDto> {
        val kyc = kycService.findKYCByUserId(user.userId)
            ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        return ResponseEntity(kyc, HttpStatus.OK)
    }
}


