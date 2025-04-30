package com.fueltracker.domain

import com.fueltracker.domain.repository.FuelTrackerRepository

class GetCarDataUseCase(
    private val fuelTrackerRepository: FuelTrackerRepository
) {
    fun getCarData(id: String) = fuelTrackerRepository.getCarData(carId = id)

    fun getCarId(id: String) = fuelTrackerRepository.getCarId(carId = id)
}