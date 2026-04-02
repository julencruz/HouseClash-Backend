package com.houseclash.backend.domain.usecase

import com.houseclash.backend.domain.model.HouseDetails
import com.houseclash.backend.domain.port.HouseRepository
import com.houseclash.backend.domain.port.UserRepository


class GetHouseDetailsUsecase(
    private val userRepository: UserRepository,
    private val houseRepository: HouseRepository
) {
    fun execute(userId: Long): HouseDetails {
        val user = userRepository.findById(userId)
        require(user != null) { "User not found" }
        require(user.houseId != null) { "User does not belong to a house" }

        val house = houseRepository.findById(user.houseId)
        require(house != null) { "House not found" }

        val members = userRepository.findByHouseId(user.houseId)

        return HouseDetails(house, members)
    }
}
