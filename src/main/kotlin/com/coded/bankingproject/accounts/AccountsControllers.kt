package com.coded.bankingproject.accounts

import com.coded.bankingproject.accounts.dtos.*
import com.coded.bankingproject.accounts.exceptions.AccountExceptionBase
import com.coded.bankingproject.accounts.exceptions.AccountLimitException
import com.coded.bankingproject.accounts.exceptions.IllegalTransferException
import com.coded.bankingproject.domain.entities.AccountEntity
import com.coded.bankingproject.domain.projections.AccountListItemProjection
import com.coded.bankingproject.services.AccountService
import com.coded.bankingproject.services.TransactionService
import com.coded.bankingproject.services.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import javax.security.auth.login.AccountException

@RestController
@RequestMapping("/accounts/v1/accounts")
class AccountsControllers(
    private val accountService: AccountService,
    private val userService: UserService,
    private val transactionService: TransactionService
) {

    @GetMapping
    fun getAllAccounts(
        authentication: Authentication
        ): List<AccountListItemProjection> {
        val userDetails = authentication.principal as UserDetails
        val userEntity = userService.findUserByUsername(userDetails.username)
            ?: throw AccountException("User not found")

        return accountService.getAccountByUserId(userEntity.id!!)
    }

    @PostMapping
    fun createAccount(
        @Valid @RequestBody accountCreateRequestDto : AccountCreateRequestDto,
        authentication: Authentication
    ) : ResponseEntity<Any>
    {
        val userDetails = authentication.principal as UserDetails
        val user = userService.findUserByUsername(userDetails.username)
            ?: return ResponseEntity(null, HttpStatus.BAD_REQUEST)

        return try {
            val account = accountService.createAccount(accountCreateRequestDto.toEntity(user))
            ResponseEntity(account, HttpStatus.CREATED)
        } catch (e: AccountLimitException ) {
            ResponseEntity(e.code, HttpStatus.BAD_REQUEST)
        } catch (e: Exception) {
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }


    @PostMapping(path=["/transfer"])
    fun transfer(
        @Valid @RequestBody transferCreateRequestDto: TransferCreateRequestDto,
        authentication: Authentication
    ): ResponseEntity<Any> {
        return try {
            val userDetails = authentication.principal as UserDetails
            val userEntity = userService.findUserByUsername(userDetails.username)
            ?: throw AccountException("User not found")

            val result = transactionService.transfer(transferCreateRequestDto, userEntity)
            ResponseEntity(result.toUpdatedBalanceResponse(), HttpStatus.OK)
        } catch (e: AccountExceptionBase) {
            ResponseEntity(e.code, HttpStatus.BAD_REQUEST)
        } catch (e: Exception) {
            ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PostMapping(path=["/{accountNumber}/close"])
    fun closeAccount(@PathVariable accountNumber : String, authentication: Authentication) {
        val userDetails = authentication.principal as UserDetails
        val userEntity = userService.findUserByUsername(userDetails.username)
        ?: throw AccountException("User not found")
        accountService.closeAccount(accountNumber, userEntity)
    }
}