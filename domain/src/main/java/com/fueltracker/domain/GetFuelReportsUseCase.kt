package com.fueltracker.domain

import com.fueltracker.domain.repository.FuelTrackerRepository

class GetFuelReportsUseCase(
    private val fuelTrackerRepository: FuelTrackerRepository
) {
    suspend fun getReports(id: String) = fuelTrackerRepository.getFuelReports(userId = id)
}