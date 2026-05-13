package com.houseclash.backend.helper

import com.houseclash.backend.domain.model.ActivityLog
import com.houseclash.backend.domain.port.ActivityLogRepository

class ActivityLogRepositoryTester : ActivityLogRepository {
    val logs = mutableListOf<ActivityLog>()
    private var idCounter = 1L

    override fun save(log: ActivityLog): ActivityLog {
        val saved = if (log.id == null) log.copy(id = idCounter++) else log
        logs.removeIf { it.id == saved.id }
        logs.add(saved)
        return saved
    }

    override fun findByHouseIdOrderByCreatedAtAsc(houseId: Long): List<ActivityLog> {
        return logs.filter { it.houseId == houseId }.sortedBy { it.createdAt }
    }

    override fun findByHouseIdOrderByCreatedAtDesc(houseId: Long, page: Int, size: Int): List<ActivityLog> {
        val sorted = logs.filter { it.houseId == houseId }.sortedByDescending { it.createdAt }
        val fromIndex = page * size
        if (fromIndex >= sorted.size) return emptyList()
        return sorted.subList(fromIndex, minOf(fromIndex + size, sorted.size))
    }

    override fun countByHouseId(houseId: Long): Long {
        return logs.count { it.houseId == houseId }.toLong()
    }

    override fun deleteOldLogsExceptPendingReview(houseId: Long, pendingTaskIds: List<Long>) {
        if (pendingTaskIds.isEmpty()) {
            logs.removeIf { it.houseId == houseId }
        } else {
            logs.removeIf { it.houseId == houseId && (it.taskId == null || it.taskId !in pendingTaskIds) }
        }
    }
}
