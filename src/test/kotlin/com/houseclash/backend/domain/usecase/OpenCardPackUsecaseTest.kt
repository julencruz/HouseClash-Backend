package com.houseclash.backend.domain.usecase

import com.houseclash.backend.helper.CardRepositoryTester
import com.houseclash.backend.helper.TestDataFactory
import com.houseclash.backend.helper.UserRepositoryTester
import com.houseclash.backend.helper.HouseRepositoryTester
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class OpenCardPackUsecaseTest {
    private val userRepository = UserRepositoryTester()
    private val houseRepository = HouseRepositoryTester()
    private val cardRepository = CardRepositoryTester()
    private val usecase = OpenCardPackUsecase(userRepository, cardRepository)

    private val userWithoutKudos = TestDataFactory.createUser(userRepository, "Poor", "poor@email.com")
    private val richUser = TestDataFactory.createUser(userRepository, "Rich", "rich@email.com", kudosBalance = 100)
    private val house = TestDataFactory.createHouse(houseRepository, userRepository, richUser)
    private val updatedRichUser = userRepository.findById(richUser.id!!)!!

    @Test
    fun `should return 4 cards on successful pack opening`() {
        val cards = usecase.execute(updatedRichUser.id!!)
        assertEquals(4, cards.size)
    }

    @Test
    fun `should deduct kudos after opening pack`() {
        usecase.execute(updatedRichUser.id!!)
        val userAfter = userRepository.findById(updatedRichUser.id)!!
        assertEquals(50, userAfter.kudosBalance)
    }

    @Test
    fun `should save cards with correct userId`() {
        val cards = usecase.execute(updatedRichUser.id!!)
        assertTrue(cards.all { it.userId == updatedRichUser.id })
    }

    @Test
    fun `should throw when user does not exist`() {
        assertThrows(IllegalArgumentException::class.java) {
            usecase.execute(999L)
        }
    }

    @Test
    fun `should throw when user has insufficient kudos`() {
        val house2 = TestDataFactory.createHouse(houseRepository, userRepository, userWithoutKudos)
        val updatedPoorUser = userRepository.findById(userWithoutKudos.id!!)!!
        assertThrows(IllegalArgumentException::class.java) {
            usecase.execute(updatedPoorUser.id!!)
        }
    }

    @Test
    fun `should throw when user does not belong to a house`() {
        val homeless = TestDataFactory.createUser(userRepository, "Homeless", "homeless@email.com", kudosBalance = 100)
        assertThrows(IllegalArgumentException::class.java) {
            usecase.execute(homeless.id!!)
        }
    }
}
