package com.houseclash.backend.infrastructure.web.activitylog

import com.houseclash.backend.domain.usecase.GetActivityLogUsecase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/houses/{houseId}/activity-log")
@Tag(name = "Activity Log", description = "Muro de notificaciones de la llar")
class ActivityLogController(
    private val getActivityLogUsecase: GetActivityLogUsecase
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Operation(
        summary = "Obtenir el muro de notificaciones",
        description = "Retorna tots els events recents de la llar ordenats per data ascendent (els més recents al fons). Les entrades amb isPendingReview=true poden ser aprovades o disputades."
    )
    @GetMapping
    fun getActivityLog(
        @PathVariable houseId: Long,
        authentication: Authentication
    ): ResponseEntity<List<ActivityLogResponse>> {
        val userId = authentication.principal as Long
        logger.info("User {} fetching activity log for house {}", userId, houseId)
        val entries = getActivityLogUsecase.execute(houseId)
        return ResponseEntity.ok(entries.map { it.toResponse() })
    }

    @Operation(
        summary = "Obtenir el muro de notificaciones paginat",
        description = "Retorna els events de la llar paginats, ordenats del més recent al més antic. Útil per a carregar el log de forma incremental."
    )
    @GetMapping("/paged")
    fun getActivityLogPaged(
        @PathVariable houseId: Long,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        authentication: Authentication
    ): ResponseEntity<PagedActivityLogResponse> {
        val userId = authentication.principal as Long
        logger.info("User {} fetching paged activity log for house {}, page={}, size={}", userId, houseId, page, size)
        val result = getActivityLogUsecase.executePaged(houseId, page, size)
        return ResponseEntity.ok(result.toResponse())
    }
}
