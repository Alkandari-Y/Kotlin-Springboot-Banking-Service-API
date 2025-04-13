package com.coded.bankingproject.accounts

import com.coded.bankingproject.repository.AccountRepository
import com.coded.bankingproject.services.AccountService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/accounts/v1")
class AccountsControllers(private val accountService: AccountService) {

    @GetMapping(path = ["/accounts"])
    fun getAllAccounts() = accountService.getAllAccounts()
}