package com.coded.bankingproject.services

import com.coded.bankingproject.domain.entities.UserEntity
import com.coded.bankingproject.errors.ErrorCode
import com.coded.bankingproject.repositories.UserRepository
import com.coded.bankingproject.users.exceptions.UserExistsException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository
): UserService {
    override fun createUser(user: UserEntity): UserEntity {
        val usernameExists = userRepository.findByUsername(user.username)

        if (usernameExists != null) {
            throw UserExistsException("Username already exists", code = ErrorCode.ACCOUNT_USER_ALREADY_EXISTS)
        }

        return userRepository.save(user)
    }

    override fun findUserById(userId: Long): UserEntity? {
        return userRepository.findByIdOrNull(userId)
    }

    override fun findUserByUsername(username: String): UserEntity? {
        return userRepository.findByUsername(username)
    }
}