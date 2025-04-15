package com.coded.bankingproject.users

import com.coded.bankingproject.services.KYCService
import com.coded.bankingproject.services.UserService
import com.coded.bankingproject.users.dtos.*
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users/v1")
class UserController(
    private val userService: UserService,
    private val kycService: KYCService
) {

    @PostMapping(path=["/register"])
    fun register(@Valid @RequestBody registerRequestDto: RegisterCreateRequestDto): ResponseEntity<Nothing> {
        return try {
            userService.createUser(registerRequestDto.toEntity())
            ResponseEntity(null, HttpStatus.OK)
        } catch (e: Exception) {
            ResponseEntity(null, HttpStatus.BAD_REQUEST)
        }
    }

    @PostMapping(path=["/kyc"])
    fun updateKYC(
        @Valid @RequestBody kycCreateRequestDto: KYCCreateRequestDto
    ): ResponseEntity<KYCCreateRequestDto> {
        return try {
            kycService.createKYCOrUpdate(kycCreateRequestDto)
            ResponseEntity(kycCreateRequestDto, HttpStatus.CREATED)
        }  catch (e: Exception) {
            ResponseEntity(null, HttpStatus.BAD_REQUEST)
        }
    }

    @GetMapping(path=["/kyc/{userId}"])
    fun getKYCByUserId(
        @PathVariable("userId") userId: Long
    ): ResponseEntity<KYCResponseDto> {
        val kyc = kycService.findKYCByUserId(userId)
            ?: return ResponseEntity(HttpStatus.NOT_FOUND)
        return ResponseEntity(kyc, HttpStatus.OK)
    }
}