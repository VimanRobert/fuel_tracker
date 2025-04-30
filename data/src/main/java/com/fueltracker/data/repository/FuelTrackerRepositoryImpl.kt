package com.fueltracker.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.fueltracker.domain.model.Car
import com.fueltracker.domain.model.Report
import com.fueltracker.domain.model.User
import com.fueltracker.domain.repository.FuelTrackerRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FuelTrackerRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val sharedPrefs: SharedPreferences
): FuelTrackerRepository {

    override suspend fun getFuelReports(userId: String): List<Report> {
        val snapshot = firestore.collection("users").document(userId)
            .collection("reports").get().await()
        return snapshot.toObjects(Report::class.java)
    }

    override suspend fun getUserData(userId: String): User? {
        val snapshot = firestore.collection("users").document(userId).get().await()
        return snapshot.toObject(User::class.java)
    }

    override fun setUserData(userId: String, userEmail: String, carCategory: Car?): User {
        TODO("Not yet implemented")
    }

    override fun setCarData(
        carId: String,
        brand: String,
        type: String,
        fuelType: String,
        engine: Double,
        horsePower: Int
    ): Car {
        TODO("Not yet implemented")
    }

    override fun getCarData(carId: String): Car {
        return Car(
            carId = carId,
            brand = sharedPrefs.getString("brand", "") ?: "",
            type = sharedPrefs.getString("model", "") ?: "",
            engine = sharedPrefs.getFloat("engine", 0.0F),
            horsePower = sharedPrefs.getInt("horsePower", 0),
            fuelType = sharedPrefs.getString("fuelType", "") ?: ""
        )
    }

    override fun getCarId(carId: String): String {
        TODO("Not yet implemented")
    }
}