package com.houseclash.backend.domain.usecase

import com.houseclash.backend.domain.model.ActivityLog
import com.houseclash.backend.domain.model.ActivityLogType
import com.houseclash.backend.domain.model.TaskStatus
import com.houseclash.backend.domain.port.ActivityLogRepository
import com.houseclash.backend.domain.port.TaskRepository

class GetActivityLogUsecase(
    private val activityLogRepository: ActivityLogRepository,
    private val taskRepository: TaskRepository,
) {
    fun execute(houseId: Long): List<ActivityLogEntry> {
        val logs = activityLogRepository.findByHouseIdOrderByCreatedAtAsc(houseId)
        val pendingTaskIds = taskRepository
            .findByHouseIdAndStatus(houseId, TaskStatus.PENDING_REVIEW)
            .map { it.id!! }
            .toSet()

        return logs.map { log ->
            ActivityLogEntry(
                log = log,
                isPendingReview = log.taskId != null && log.taskId in pendingTaskIds
                        && log.type == ActivityLogType.TASK_COMPLETED
            )
        }
    }

    fun executePaged(houseId: Long, page: Int, size: Int): PagedActivityLogResult {
        val totalElements = activityLogRepository.countByHouseId(houseId)
        val totalPages = if (totalElements == 0L) 1 else ((totalElements + size - 1) / size).toInt()

        val logs = activityLogRepository.findByHouseIdOrderByCreatedAtDesc(houseId, page, size)
        val pendingTaskIds = taskRepository
            .findByHouseIdAndStatus(houseId, TaskStatus.PENDING_REVIEW)
            .map { it.id!! }
            .toSet()

        val entries = logs.map { log ->
            ActivityLogEntry(
                log = log,
                isPendingReview = log.taskId != null && log.taskId in pendingTaskIds
                        && log.type == ActivityLogType.TASK_COMPLETED
            )
        }

        return PagedActivityLogResult(
            content = entries,
            page = page,
            size = size,
            totalElements = totalElements,
            totalPages = totalPages,
            isLast = page >= totalPages - 1
        )
    }
}

data class ActivityLogEntry(
    val log: ActivityLog,
    val isPendingReview: Boolean
)

data class PagedActivityLogResult(
    val content: List<ActivityLogEntry>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
    val isLast: Boolean
)
