package com.coded.bankingproject.services

import com.coded.bankingproject.domain.entities.AccountEntity
import com.coded.bankingproject.domain.entities.UserEntity
import com.coded.bankingproject.domain.projections.AccountListItemProjection

interface AccountService {
    fun getAllAccounts(): List<AccountListItemProjection>
    fun getAccountByUserId(userId: Long): List<AccountListItemProjection>
    fun createAccount(accountEntity: AccountEntity): AccountEntity
    fun closeAccount(accountNumber: String, user: UserEntity): Unit
}