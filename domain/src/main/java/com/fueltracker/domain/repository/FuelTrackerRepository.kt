package com.fueltracker.domain.repository

import com.fueltracker.domain.model.Car
import com.fueltracker.domain.model.Report
import com.fueltracker.domain.model.User

interface FuelTrackerRepository {

    fun setUserData(userId: String, userEmail: String, carCategory: Car?): User

    fun getUserData(userId: String): User

    fun setCarData(
        carId: String,
        brand: String,
        type: String,
        fuelType: String,
        engine: Number,
        horsePower: Number,
        energy: Number,
    ): Car

    fun getCarData(carId: String): Car

    fun getFuelReports(): List<Report>
}