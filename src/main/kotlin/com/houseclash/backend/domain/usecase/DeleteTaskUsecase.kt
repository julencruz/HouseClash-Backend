package com.houseclash.backend.domain.usecase

import com.houseclash.backend.domain.model.TaskStatus
import com.houseclash.backend.domain.port.HouseRepository
import com.houseclash.backend.domain.port.TaskRepository

class DeleteTaskUsecase(
    private val taskRepository: TaskRepository,
    private val houseRepository: HouseRepository
) {
    fun execute(userId: Long, taskId: Long) {
        val task = taskRepository.findById(taskId)
        require(task != null) { "Task does not exist" }

        val house = houseRepository.findById(task.houseId)
        require(house != null) { "House not found" }
        require(house.createdBy == userId) { "Only the house admin can delete tasks" }

        require(task.status !in listOf(TaskStatus.APPROVED, TaskStatus.AUTO_APPROVED)) { "Approved tasks cannot be deleted to preserve the kudos history" }

        taskRepository.delete(taskId)
    }
}
