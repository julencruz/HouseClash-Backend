package com.houseclash.backend.domain.usecase

import com.houseclash.backend.domain.port.HouseRepository

class RotateInviteCodesUsecase(
    private val houseRepository: HouseRepository,
) {
    fun execute() {
        houseRepository.findAll().forEach { house ->
            houseRepository.save(house.regenerateInviteCode())
        }
    }
}
