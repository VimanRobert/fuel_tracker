package com.fueltracker.domain

import com.fueltracker.domain.repository.FuelTrackerRepository

class GetFuelReportsUseCase(
    private val fuelTrackerRepository: FuelTrackerRepository
) {
    fun getReports() = fuelTrackerRepository.getFuelReports()
}