package com.houseclash.backend.domain.usecase

import com.houseclash.backend.domain.model.Card
import com.houseclash.backend.domain.model.CardType
import com.houseclash.backend.domain.port.CardRepository
import com.houseclash.backend.domain.port.UserRepository
import kotlin.random.Random

class OpenCardPackUsecase(
    private val userRepository: UserRepository,
    private val cardRepository: CardRepository
) {
    companion object {
        const val PACK_COST = 50
        const val CARDS_PER_PACK = 4
    }

    fun execute(userId: Long): List<Card> {
        val user = userRepository.findById(userId)
        require(user != null) { "User does not exist" }
        require(user.houseId != null) { "User must belong to a house to open a card pack" }

        val updatedUser = user.spendKudos(PACK_COST)
        userRepository.save(updatedUser)

        return (1..CARDS_PER_PACK).map {
            val cardType = drawRandomCardType()
            cardRepository.save(Card.create(userId, cardType))
        }
    }

    private fun drawRandomCardType(): CardType {
        val totalWeight = CardType.entries.sumOf { it.probability }
        require(totalWeight > 0) { "Invalid card weights configuration" }

        var random = Random.nextInt(totalWeight)
        for (cardType in CardType.entries) {
            random -= cardType.probability
            if (random < 0) return cardType
        }

        throw IllegalStateException("Failed to draw card")
    }
}
