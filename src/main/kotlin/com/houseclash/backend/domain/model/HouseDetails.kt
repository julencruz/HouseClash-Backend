package com.houseclash.backend.domain.model

data class HouseDetails(
    val house: House,
    val members: List<User>
)
