package com.coded.bankingproject.services

import com.coded.bankingproject.domain.entities.UserEntity
import com.coded.bankingproject.repositories.UserRepository
import com.coded.bankingproject.users.exceptions.UserExistsException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository
): UserService {
    override fun createUser(user: UserEntity) {
        val usernameExists = userRepository.findByUsername(user.username)

        if (usernameExists != null) {
            throw UserExistsException("Username already exists")
        }

        userRepository.save(user)
    }

    override fun findUserById(userId: Long): UserEntity? {
        return userRepository.findByIdOrNull(userId)
    }
}