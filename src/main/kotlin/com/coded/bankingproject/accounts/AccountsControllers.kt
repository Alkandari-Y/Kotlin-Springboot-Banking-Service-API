package com.coded.bankingproject.accounts

import com.coded.bankingproject.accounts.dtos.*
import com.coded.bankingproject.domain.entities.AccountEntity
import com.coded.bankingproject.services.AccountService
import com.coded.bankingproject.services.TransactionService
import com.coded.bankingproject.services.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/accounts/v1/accounts")
class AccountsControllers(
    private val accountService: AccountService,
    private val userService: UserService,
    private val transactionService: TransactionService
) {

    @GetMapping
    fun getAllAccounts() = accountService.getAllAccounts()

    @PostMapping
    fun createAccount(
        @Valid @RequestBody accountCreateRequestDto : AccountCreateRequestDto
    ) : ResponseEntity<AccountEntity>
    {
        val user = userService.findUserById(accountCreateRequestDto.userId)
            ?: return ResponseEntity(null, HttpStatus.BAD_REQUEST)

        val account = accountService.createAccount(accountCreateRequestDto.toEntity(user))
        return ResponseEntity(account, HttpStatus.CREATED)
    }


    @PostMapping(path=["/transfer"])
    fun transfer(
        @Valid @RequestBody transferCreateRequestDto: TransferCreateRequestDto
    ): ResponseEntity<UpdatedBalanceResponse> {
        val result = transactionService.transfer(transferCreateRequestDto)
        return ResponseEntity(result.toUpdatedBalanceResponse(), HttpStatus.OK)
    }

    @PostMapping(path=["/{accountNumber}/close"])
    fun closeAccount(@PathVariable accountNumber : String) {
        accountService.closeAccount(accountNumber)
    }
}