package com.houseclash.backend.domain.usecase

import com.houseclash.backend.domain.model.Category
import com.houseclash.backend.domain.port.CategoryRepository
import com.houseclash.backend.domain.port.UserRepository

class GetHouseCategoriesUsecase(
    private val userRepository: UserRepository,
    private val categoryRepository: CategoryRepository
) {
    fun execute(userId: Long): List<Category> {
        val user = userRepository.findById(userId)
        require(user != null) { "User not found" }
        require(user.houseId != null) { "User does not belong to a house" }

        return categoryRepository.findByHouseId(user.houseId)
    }
}