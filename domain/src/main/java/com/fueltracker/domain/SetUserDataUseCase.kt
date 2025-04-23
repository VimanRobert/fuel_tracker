package com.fueltracker.domain

import com.fueltracker.domain.model.Car
import com.fueltracker.domain.repository.FuelTrackerRepository

class SetUserDataUseCase(
    private val fuelTrackerRepository: FuelTrackerRepository
) {
    fun setData(id: String, email: String, car: Car) =
        fuelTrackerRepository.setUserData(userId = id, userEmail = email, carCategory = car)
}