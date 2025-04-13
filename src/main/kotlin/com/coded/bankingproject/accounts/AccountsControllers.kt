package com.coded.bankingproject.accounts

import com.coded.bankingproject.accounts.dtos.AccountCreateRequestDto
import com.coded.bankingproject.accounts.dtos.toEntity
import com.coded.bankingproject.domain.entities.AccountEntity
import com.coded.bankingproject.repository.AccountRepository
import com.coded.bankingproject.services.AccountService
import com.coded.bankingproject.services.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/accounts/v1")
class AccountsControllers(
    private val accountService: AccountService,
    private val userService: UserService
) {

    @GetMapping(path = ["/accounts"])
    fun getAllAccounts() = accountService.getAllAccounts()

    @PostMapping(path=["/accounts"])
    fun createAccount(
        @RequestBody accountCreateRequestDto : AccountCreateRequestDto
    ) : ResponseEntity<AccountEntity>
    {
        val user = userService.findUserById(accountCreateRequestDto.userId)
            ?: return ResponseEntity(null, HttpStatus.OK)

        val account = accountService.createAccount(accountCreateRequestDto.toEntity(user))
        return ResponseEntity(account, HttpStatus.CREATED)
    }
}