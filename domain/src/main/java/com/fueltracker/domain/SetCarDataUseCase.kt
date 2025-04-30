package com.fueltracker.domain

import android.content.Context
import com.fueltracker.domain.model.Car
import com.fueltracker.domain.repository.FuelTrackerRepository
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

    fun setCarDefaultData() = fuelTrackerRepository.setCarData(
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
        val prefs = context.getSharedPreferences("car_config", Context.MODE_PRIVATE)
        with(prefs.edit()) {
            putString("carId", car.carId)
            putString("brand", car.brand)
            putString("model", car.type)
            putString("fuelType", car.fuelType)
            putString("horsePower", car.horsePower.toString())
            putString("engine", car.engine.toString())
            apply()
        }
    }

    fun generateCar(context: Context): Car {
        return Car(
            carId = setCarId(context),
            brand = "Renault",
            type = "Megane 4",
            fuelType = "Diesel",
            horsePower = 110,
            engine = 1.5f
        )
    }

    fun carDataGenerator(context: Context): Car {
        val randomAvailableCarConfigList = listOf(
            "renault_meg_3",
            "renault_meg_4",
            "skoda_oc_1",
            "skoda_oc_2",
            "audi_a4",
            "dacia_sa",
            "dacia_du"
        )

        return when (randomAvailableCarConfigList.random()) {
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

            "skoda_oc_1" -> TODO()
            "skoda_oc_2" -> TODO()
            "audi_a4" -> TODO()
            "dacia_sa" -> TODO()
            "dacia_du" -> TODO()
            else -> {
                setCarDefaultData()
            }
        }
    }
}
