package com.houseclash.backend.domain.usecase

import com.houseclash.backend.domain.model.Task
import com.houseclash.backend.domain.model.TaskStatus
import com.houseclash.backend.domain.port.TaskRepository
import com.houseclash.backend.domain.port.UserRepository

class GetActiveTasksUsecase(
    private val userRepository: UserRepository,
    private val taskRepository: TaskRepository
) {
    fun execute(userId: Long): List<Task> {
        val user = userRepository.findById(userId)
        require(user != null) { "User not found" }
        require(user.houseId != null) { "User does not belong to a house" }

        val allHouseTasks = taskRepository.findByHouseId(user.houseId)

        val activeStatuses = setOf(
            TaskStatus.OPEN,
            TaskStatus.ASSIGNED,
            TaskStatus.PENDING_REVIEW,
            TaskStatus.DISPUTED
        )

        return allHouseTasks.filter { it.status in activeStatuses }
    }
}