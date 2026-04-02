package com.houseclash.backend.domain.usecase

import com.houseclash.backend.domain.port.CategoryRepository
import com.houseclash.backend.domain.port.HouseRepository
import com.houseclash.backend.domain.port.TaskRepository
import com.houseclash.backend.domain.port.UserRepository

class DeleteCategoryUsecase(
    private val categoryRepository: CategoryRepository,
    private val userRepository: UserRepository,
    private val taskRepository: TaskRepository,
    private val houseRepository: HouseRepository
) {
    fun execute(userId: Long, categoryId: Long) {
        val user = userRepository.findById(userId)
        require(user != null) { "User not found" }

        val category = categoryRepository.findById(categoryId)
        require(category != null) { "Category not found" }
        require(category.houseId == user.houseId) { "Cannot delete categories outside your house" }

        val house = houseRepository.findById(category.houseId)
        require(house != null) { "House not found" }
        require(house.createdBy == userId) { "Only the house captain can delete categories." }

        val tasksInCategory = taskRepository.findByCategoryId(categoryId)
        require(tasksInCategory.isEmpty()) {
            "Cannot delete a category that contains tasks. Move or delete the tasks first."
        }

        categoryRepository.delete(categoryId)
    }
}
