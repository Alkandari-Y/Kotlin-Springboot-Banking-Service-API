package com.coded.bankingproject.services

import com.coded.bankingproject.domain.entities.AccountEntity
import com.coded.bankingproject.domain.projections.AccountListItemProjection

interface AccountService {
    fun getAllAccounts(): List<AccountListItemProjection>
}