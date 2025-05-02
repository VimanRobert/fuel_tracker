package com.fueltracker.domain.model

data class Car(
    val carId: String? = "",
    val brand: String? = "",
    val type: String? = "",
    val fuelType: String? = "",
    val engine: Float? = 0F,
    val horsePower: Int? = 0,
)
