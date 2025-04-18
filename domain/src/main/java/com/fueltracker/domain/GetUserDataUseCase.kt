package com.fueltracker.domain

import com.fueltracker.data.model.FuelTrackerImpl

class GetUserDataUseCase(
    private val fuelTrackerImpl: FuelTrackerImpl
) {
    fun getData(id: String) = fuelTrackerImpl.getUserData(userId = id)
}