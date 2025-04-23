package com.fueltracker.domain

import com.fueltracker.domain.repository.FuelTrackerRepository

class GetCarDataUseCase(
    private val fuelTrackerRepository: FuelTrackerRepository
) {
    fun getData(id: String) = fuelTrackerRepository.getCarData(carId = id)
}