package com.fueltracker.domain

import com.fueltracker.data.model.FuelTrackerImpl

class SetCarDataUseCase(
    private val fuelTrackerImpl: FuelTrackerImpl
) {

    fun setData(
        carId: String,
        brand: String,
        type: String,
        fuelType: String,
        engine: Number,
        horsePower: Number,
        energy: Number,
    ) = fuelTrackerImpl.setCarData(
        carId = carId,
        brand = brand,
        type = type,
        fuelType =fuelType,
        energy = engine,
        horsePower = horsePower,
        engine = energy
    )

    fun setDefaultData() = fuelTrackerImpl.setCarData(
        carId = "00000",
        brand = "UNKNOWN",
        type = "UNKNOWN",
        fuelType = "UNKNOWN",
        energy = 0.0,
        horsePower = 0,
        engine = 0.0
    )
}
