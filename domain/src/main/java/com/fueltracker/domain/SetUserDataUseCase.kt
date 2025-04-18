package com.fueltracker.domain

import com.fueltracker.data.Car
import com.fueltracker.data.model.FuelTrackerImpl

class SetUserDataUseCase(
    private val fuelTrackerImpl: FuelTrackerImpl
) {
    fun setData(id: String, email: String, car: Car) =
        fuelTrackerImpl.setUserData(userId = id, userEmail = email, carCategory = car)
}