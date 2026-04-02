package com.houseclash.backend.domain.port

import com.houseclash.backend.domain.model.Card

interface CardRepository {

    fun save(card: Card) : Card
    fun findByUserId(userId: Long): List<Card>
    fun deleteByUserId(userId: Long)
    fun findById(cardId: Long): Card?
    fun delete(cardId: Long)
}
