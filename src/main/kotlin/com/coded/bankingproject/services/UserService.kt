package com.coded.bankingproject.services

import com.coded.bankingproject.domain.entities.UserEntity

interface UserService {
    fun createUser(user: UserEntity): UserEntity
    fun findUserById(userId: Long): UserEntity?
    fun findUserByUsername(username: String): UserEntity?
}