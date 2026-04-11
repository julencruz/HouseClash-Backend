package com.houseclash.backend.domain.model.cardlogic.effect

import com.houseclash.backend.domain.model.cardlogic.CardEffect
import com.houseclash.backend.domain.model.cardlogic.CardEffectContext
import com.houseclash.backend.domain.model.cardlogic.CardEffectResult

class StealKudosEffect : CardEffect {
    override fun execute(context: CardEffectContext): CardEffectResult {
        require(context.targetUser != null) { "A target user must be selected" }
        require(context.targetUser.id != context.executingUser.id) {
            "You cannot steal from yourself"
        }

        val stealAmount = minOf((1..6).random(), (1..6).random())

        val updatedTarget = context.targetUser.penalizeKudos(stealAmount)
        val updatedExecutor = context.executingUser.addKudos(stealAmount)

        return CardEffectResult(
            updatedUsers = listOf(updatedTarget, updatedExecutor),
            description = "You stole $stealAmount Kudos from ${context.targetUser.username}!"
        )
    }
}
