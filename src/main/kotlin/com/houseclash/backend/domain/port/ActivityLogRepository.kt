package com.houseclash.backend.domain.port

import com.houseclash.backend.domain.model.ActivityLog

interface ActivityLogRepository {
    fun save(log: ActivityLog): ActivityLog
    fun findByHouseIdOrderByCreatedAtAsc(houseId: Long): List<ActivityLog>
    fun findByHouseIdOrderByCreatedAtDesc(houseId: Long, page: Int, size: Int): List<ActivityLog>
    fun countByHouseId(houseId: Long): Long
    fun deleteOldLogsExceptPendingReview(houseId: Long, pendingTaskIds: List<Long>)
}
