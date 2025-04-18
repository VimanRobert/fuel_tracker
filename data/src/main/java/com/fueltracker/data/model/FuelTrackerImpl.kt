package com.fueltracker.data.model

import com.fueltracker.data.Car
import com.fueltracker.data.Report
import com.fueltracker.data.User

interface FuelTrackerImpl {

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