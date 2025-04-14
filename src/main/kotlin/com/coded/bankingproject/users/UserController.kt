package com.coded.bankingproject.users

import com.coded.bankingproject.services.KYCService
import com.coded.bankingproject.services.UserService
import com.coded.bankingproject.users.dtos.*
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
    fun register(@RequestBody requestDto: RegisterCreateRequestDto): ResponseEntity<Nothing> {
        return try {
            userService.createUser(requestDto.toEntity())
            ResponseEntity(null, HttpStatus.OK)
        } catch (e: Exception) {
            println(e)
            ResponseEntity(null, HttpStatus.BAD_REQUEST)
        }
    }

    @PostMapping(path=["/kyc"])
    fun updateKYC(@RequestBody kycCreateRequest: KYCCreateRequestDto): KYCCreateRequestDto {
        kycService.createKYC(kycCreateRequest)
        return kycCreateRequest
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