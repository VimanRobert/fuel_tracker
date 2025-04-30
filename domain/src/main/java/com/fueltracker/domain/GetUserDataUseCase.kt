package com.fueltracker.domain

import com.fueltracker.domain.repository.FuelTrackerRepository

class GetUserDataUseCase(
    private val fuelTrackerRepository: FuelTrackerRepository
) {
    suspend fun getData(id: String) = fuelTrackerRepository.getUserData(userId = id)
}