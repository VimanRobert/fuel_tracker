package com.fueltracker.domain

import com.fueltracker.data.model.FuelTrackerImpl

class GetFuelReportsUseCase(
    private val fuelTrackerImpl: FuelTrackerImpl
) {
    fun getReports() = fuelTrackerImpl.getFuelReports()
}