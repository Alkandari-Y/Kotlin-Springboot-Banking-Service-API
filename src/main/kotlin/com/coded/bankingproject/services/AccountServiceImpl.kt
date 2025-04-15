package com.coded.bankingproject.services

import com.coded.bankingproject.accounts.exceptions.AccountLimitException
import com.coded.bankingproject.domain.entities.AccountEntity
import com.coded.bankingproject.domain.projections.AccountListItemProjection
import com.coded.bankingproject.repositories.AccountRepository
import org.springframework.stereotype.Service

val MAX_ACCOUNT_LIMIT = 3

@Service
class AccountServiceImpl(
    private val accountRepository: AccountRepository,
): AccountService {
    override fun getAllAccounts(): List<AccountListItemProjection> {
        return accountRepository.allAccounts()
    }

    override fun createAccount(accountEntity: AccountEntity): AccountEntity {
        val user = accountEntity.user ?: throw IllegalArgumentException("User is required")
        val userId = user.id ?: throw IllegalArgumentException("User ID is required")
        val numOfCustomerAccount = accountRepository.findAllByUserId(userId)
        if (numOfCustomerAccount >= MAX_ACCOUNT_LIMIT) {
            throw AccountLimitException()
        }
        return accountRepository.save(accountEntity)
    }

    override fun closeAccount(accountNumber: String) {
        accountRepository.findByAccountNumber(accountNumber)?.apply {
            if (this.isActive) {
                accountRepository.save(this.copy(isActive = false))
            }
        }
    }
}