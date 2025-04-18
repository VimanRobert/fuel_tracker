package com.fueltracker.domain

import com.fueltracker.data.model.FuelTrackerImpl

class GetCarDataUseCase(
    private val fuelTrackerImpl: FuelTrackerImpl
) {
    fun getData(id: String) = fuelTrackerImpl.getCarData(carId = id)
}