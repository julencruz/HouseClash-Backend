package com.houseclash.backend.domain.usecase

import com.houseclash.backend.helper.HouseRepositoryTester
import com.houseclash.backend.helper.TestDataFactory
import com.houseclash.backend.helper.UserRepositoryTester
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class RotateInviteCodesUsecaseTest {

    private val userRepository = UserRepositoryTester()
    private val houseRepository = HouseRepositoryTester()
    private val usecase = RotateInviteCodesUsecase(houseRepository)

    @Test
    fun `should regenerate invite code for all houses`() {
        val user1 = TestDataFactory.createUser(userRepository, username = "User1", email = "u1@test.com")
        val user2 = TestDataFactory.createUser(userRepository, username = "User2", email = "u2@test.com")
        val house1 = TestDataFactory.createHouse(houseRepository, userRepository, user1, "Casa 1")
        val house2 = TestDataFactory.createHouse(houseRepository, userRepository, user2, "Casa 2")

        usecase.execute()

        val updatedHouse1 = houseRepository.findById(house1.id!!)!!
        val updatedHouse2 = houseRepository.findById(house2.id!!)!!

        assertNotEquals(house1.inviteCode, updatedHouse1.inviteCode)
        assertNotEquals(house2.inviteCode, updatedHouse2.inviteCode)
    }
}
