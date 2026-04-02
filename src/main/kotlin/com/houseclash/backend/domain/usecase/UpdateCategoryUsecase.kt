package com.houseclash.backend.domain.usecase

import com.houseclash.backend.domain.model.Category
import com.houseclash.backend.domain.port.CategoryRepository
import com.houseclash.backend.domain.port.HouseRepository
import com.houseclash.backend.domain.port.UserRepository

class UpdateCategoryUsecase(
    private val categoryRepository: CategoryRepository,
    private val userRepository: UserRepository,
    private val houseRepository: HouseRepository
) {
    fun execute(userId: Long, categoryId: Long, newName: String): Category {
        val user = userRepository.findById(userId)
        require(user != null) { "User does not exist" }
        val category = categoryRepository.findById(categoryId)
        require(category != null) { "Category does not exist" }
        require(category.houseId == user.houseId) { "Cannot update categories outside your house" }

        val house = houseRepository.findById(category.houseId)
        require(house != null) { "House does not exist" }
        require(house.createdBy == userId) { "Only the house captain can update categories." }

        val existing = categoryRepository.findByHouseId(user.houseId)
        require(existing.none { it.name.equals(newName, ignoreCase = true) }) { "Category name already exists" }

        val updated = category.update(newName)
        return categoryRepository.save(updated)
    }
}
