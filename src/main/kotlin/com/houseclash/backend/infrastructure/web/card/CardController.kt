package com.houseclash.backend.infrastructure.web.card

import com.houseclash.backend.domain.usecase.ExecuteCardEffectUsecase
import com.houseclash.backend.domain.usecase.GetUserCardsUsecase
import com.houseclash.backend.domain.usecase.OpenCardPackUsecase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/cards")
@Tag(name = "Cartes", description = "Gestió de les cartes especials dels usuaris")
class CardController(
    private val getUserCardsUsecase: GetUserCardsUsecase,
    private val openCardPackUsecase: OpenCardPackUsecase,
    private val executeCardEffectUsecase: ExecuteCardEffectUsecase
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Operation(summary = "Obtenir les meves cartes", description = "Retorna totes les cartes especials que té l'usuari autenticat al seu inventari")
    @GetMapping
    fun getMyCards(authentication: Authentication): ResponseEntity<List<CardResponse>> {
        val userId = authentication.principal as Long
        logger.info("User {} fetching their cards", userId)
        val cards = getUserCardsUsecase.execute(userId)
        logger.info("Returning {} cards for user {}", cards.size, userId)
        return ResponseEntity.ok(cards.map { it.toResponse() })
    }

    @Operation(summary = "Obrir un sobre de cartes", description = "Obre un sobre i afegeix un conjunt de cartes aleatòries a l'inventari de l'usuari. Requereix monedes suficients")
    @PostMapping("/open-pack")
    fun openPack(authentication: Authentication): ResponseEntity<List<CardResponse>> {
        val userId = authentication.principal as Long
        logger.info("User {} opening a card pack", userId)
        val cards = openCardPackUsecase.execute(userId)
        logger.info("User {} received {} cards from pack", userId, cards.size)
        return ResponseEntity.ok(cards.map { it.toResponse() })
    }

    @Operation(summary = "Usar una carta", description = "Activa l'efecte d'una carta de l'inventari de l'usuari. Segons el tipus de carta, cal indicar un usuari, una tasca o una categoria objectiu")
    @PostMapping("/{cardId}/use")
    fun useCard(
        @PathVariable cardId: Long,
        @RequestBody request: UseCardRequest,
        authentication: Authentication
    ): ResponseEntity<CardEffectResultResponse> {
        val userId = authentication.principal as Long
        logger.info("User {} using card {} (targetUser={}, targetTask={}, targetCategory={})",
            userId, cardId, request.targetUserId, request.targetTaskId, request.targetCategoryId)
        val result = executeCardEffectUsecase.execute(
            cardId = cardId,
            executingUserId = userId,
            targetUserId = request.targetUserId,
            targetTaskId = request.targetTaskId,
            targetCategoryId = request.targetCategoryId
        )
        logger.info("Card {} effect executed by user {}", cardId, userId)
        return ResponseEntity.ok(result.toResponse())
    }
}
