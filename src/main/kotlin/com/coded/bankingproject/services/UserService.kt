package com.coded.bankingproject.services

import com.coded.bankingproject.domain.entities.UserEntity

interface UserService {
    fun createUser(user: UserEntity): Unit
    fun findUserById(userId: Long): UserEntity?
}