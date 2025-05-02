package com.fueltracker.presentation.carstatus

import android.content.Context
import androidx.lifecycle.ViewModel
import com.fueltracker.domain.GetCarDataUseCase
import com.fueltracker.domain.SetCarDataUseCase
import com.fueltracker.domain.model.Car
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class CarStatusViewModel @Inject constructor(
    private val setCarDataUseCase: SetCarDataUseCase,
    private val getCarDataUseCase: GetCarDataUseCase
) : ViewModel() {

    fun retrieveCarId(context: Context): String {
        val currentCarId = setCarDataUseCase.setCarId(context)
        return getCarDataUseCase.getCarId(currentCarId)
    }

    fun setCarData(
        context: Context,
        carId: String,
        carBrand: String,
        carFuelType: String,
        carModel: String,
        carEngine: Double,
        carHorsePower: Int
    ) {
        setCarDataUseCase.setCarData(
            carId,
            carBrand,
            carModel,
            carFuelType,
            carEngine,
            carHorsePower
        )
    }

    fun getCarData(carId: String): Car {
        return getCarDataUseCase.getCarData(carId)
    }

    fun retrieveCarData(context: Context): Car {
        return setCarDataUseCase.carDataGenerator(context)
    }

    fun registerCarData(context: Context) {
        setCarDataUseCase.saveCarData(context, setCarDataUseCase.carDataGenerator(context))
    }
}