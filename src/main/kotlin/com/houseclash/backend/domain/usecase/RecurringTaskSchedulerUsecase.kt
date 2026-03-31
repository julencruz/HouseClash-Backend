package com.houseclash.backend.domain.usecase

import com.houseclash.backend.domain.port.TaskRepository

class RecurringTaskSchedulerUsecase(
    private val taskRepository: TaskRepository
) {
    fun execute() {
        taskRepository.findRecurringTasksDue().forEach { task ->
            if (task.isDueForReset()) {
                taskRepository.save(task.resetForNextCycle())
            }
        }
    }
}
