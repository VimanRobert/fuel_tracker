package com.fueltracker.domain.model

data class Car(
    val carId: String,
    val brand: String,
    val type: String,
    val fuelType: String,
    val engine: Double,
    val horsePower: Int,
    val energy: Double,
)
