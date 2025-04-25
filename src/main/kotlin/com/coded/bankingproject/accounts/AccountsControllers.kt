package com.coded.bankingproject.accounts

import com.coded.bankingproject.accounts.dtos.*
import com.coded.bankingproject.accounts.exceptions.AccountLimitException
import com.coded.bankingproject.domain.entities.AuthUserDetails
import com.coded.bankingproject.domain.entities.toUserEntity
import com.coded.bankingproject.domain.projections.AccountListItemProjection
import com.coded.bankingproject.services.AccountService
import com.coded.bankingproject.services.TransactionService
import com.coded.bankingproject.services.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
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
        @AuthenticationPrincipal user: AuthUserDetails
        ): List<AccountListItemProjection> {
        val userEntity = userService.findUserByUsername(user.username)
            ?: throw AccountException("User not found")

        return accountService.getAccountByUserId(userEntity.id!!)
    }

    @PostMapping
    fun createAccount(
        @Valid @RequestBody accountCreateRequestDto : AccountCreateRequestDto,
        @AuthenticationPrincipal user: AuthUserDetails
    ) : ResponseEntity<Any>
    {
        return try {
            val account = accountService.createAccount(
                accountCreateRequestDto.toEntity(user.toUserEntity())
            )

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
        @AuthenticationPrincipal user: AuthUserDetails
    ): ResponseEntity<UpdatedBalanceResponse> {
            val result = transactionService.transfer(
                transferCreateRequestDto,
                user.toUserEntity()
            )
            return ResponseEntity(result.toUpdatedBalanceResponse(), HttpStatus.OK)
    }

    @PostMapping(path=["/{accountNumber}/close"])
    fun closeAccount(
        @PathVariable accountNumber : String,
        @AuthenticationPrincipal user: AuthUserDetails
    ) {
        accountService.closeAccount(accountNumber, user.toUserEntity())
    }
}