package com.houseclash.backend.domain.usecase

import com.houseclash.backend.domain.model.User
import com.houseclash.backend.domain.port.UserRepository

class GetUserProfileUsecase(
    private val userRepository: UserRepository
) {
    fun execute(userId: Long): User {
        val user = userRepository.findById(userId)
        require(user != null) { "User not found" }
        return user
    }
}