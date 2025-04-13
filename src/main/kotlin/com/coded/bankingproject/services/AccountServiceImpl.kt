package com.coded.bankingproject.services

import com.coded.bankingproject.domain.entities.AccountEntity
import com.coded.bankingproject.domain.projections.AccountListItemProjection
import com.coded.bankingproject.repository.AccountRepository
import org.springframework.stereotype.Service

@Service
class AccountServiceImpl(
    private val accountRepository: AccountRepository,
): AccountService {
    override fun getAllAccounts(): List<AccountListItemProjection> {
        return accountRepository.allAccounts()
    }

    override fun createAccount(accountEntity: AccountEntity): AccountEntity {
        return accountRepository.save(accountEntity)
    }
}