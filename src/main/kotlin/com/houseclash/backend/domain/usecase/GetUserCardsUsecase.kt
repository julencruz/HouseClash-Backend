package com.houseclash.backend.domain.usecase

import com.houseclash.backend.domain.model.Card
import com.houseclash.backend.domain.port.CardRepository
import com.houseclash.backend.domain.port.UserRepository

class GetUserCardsUsecase(
    private val userRepository: UserRepository,
    private val cardRepository: CardRepository,
) {
    fun execute(userId: Long): List<Card> {
        val user = userRepository.findById(userId)
        require(user != null) { "User does not exist" }
        return cardRepository.findByUserId(userId)
    }
}
