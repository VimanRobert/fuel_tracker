package com.fueltracker.domain

import android.content.Context
import android.util.Log
import com.fueltracker.domain.model.Car
import com.fueltracker.domain.repository.FuelTrackerRepository
import com.google.gson.Gson
import java.util.UUID

class SetCarDataUseCase(
    private val fuelTrackerRepository: FuelTrackerRepository
) {

    fun setCarData(
        carId: String,
        brand: String,
        type: String,
        fuelType: String,
        engine: Double,
        horsePower: Int,
    ) = fuelTrackerRepository.setCarData(
        carId = carId,
        brand = brand,
        type = type,
        fuelType = fuelType,
        horsePower = horsePower,
        engine = engine
    )

    private fun setCarDefaultData() = fuelTrackerRepository.setCarData(
        carId = "00000",
        brand = "UNKNOWN",
        type = "UNKNOWN",
        fuelType = "UNKNOWN",
        horsePower = 0,
        engine = 0.0
    )

    fun setCarId(context: Context): String {
        val prefs = context.getSharedPreferences("car_config", Context.MODE_PRIVATE)
        var carId = prefs.getString("carId", null)

        if (carId == null) {
            carId = UUID.randomUUID().toString()
            prefs.edit().putString("carId", carId).apply()
        }

        return carId
    }

    fun saveCarData(context: Context, car: Car) {
        val sharedPreferences = context.getSharedPreferences("car_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(car)
        editor.putString("car_data", json)
        editor.apply()
    }

    private fun getCarData(context: Context): Car? {
        val sharedPreferences = context.getSharedPreferences("car_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("car_data", null)
        return if (json != null) gson.fromJson(json, Car::class.java) else null
    }

    fun carDataGenerator(context: Context): Car {
        val currentCarConfig = getCarData(context)
        if (currentCarConfig != null) return currentCarConfig

        val randomAvailableCarConfigList = listOf(
            "renault_meg_3",
            "renault_meg_4",
            "skoda_oc_1",
            "skoda_fab_1",
            "audi_a4",
            "dacia_sa",
            "dacia_du"
        )

        val car = when (randomAvailableCarConfigList.random()) {
            "renault_meg_3" -> Car(
                carId = setCarId(context),
                brand = "Renault",
                type = "Megane 3",
                fuelType = "Diesel",
                horsePower = 130,
                engine = 1.9f
            )

            "renault_meg_4" -> Car(
                carId = setCarId(context),
                brand = "Renault",
                type = "Megane 4",
                fuelType = "Diesel",
                horsePower = 110,
                engine = 1.5f
            )

            "skoda_oc_1" -> Car(
                carId = setCarId(context),
                brand = "Skoda",
                type = "Octavia 2",
                fuelType = "Diesel",
                horsePower = 150,
                engine = 2.0f
            )

            "skoda_fab_1" -> Car(
                carId = setCarId(context),
                brand = "Skoda",
                type = "Fabia",
                fuelType = "Gasoline",
                horsePower = 85,
                engine = 1.4f
            )

            "audi_a4" -> Car(
                carId = setCarId(context),
                brand = "Audi",
                type = "A4",
                fuelType = "Diesel",
                horsePower = 190,
                engine = 2.5f
            )

            "dacia_sa" -> Car(
                carId = setCarId(context),
                brand = "Dacia",
                type = "Sandero",
                fuelType = "Gasoline",
                horsePower = 85,
                engine = 1.2f
            )

            "dacia_du" -> Car(
                carId = setCarId(context),
                brand = "Dacia",
                type = "Duster",
                fuelType = "Diesel",
                horsePower = 115,
                engine = 1.6f
            )
            else -> {
                setCarDefaultData()
            }
        }
        saveCarData(context, car)
        return car
    }
}
