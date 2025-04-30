package com.fueltracker.presentation.utils

import android.content.Context
import android.content.SharedPreferences
import com.fueltracker.domain.GetCarDataUseCase
import com.fueltracker.domain.model.Car
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

object ConnectionHandler {

    fun connectCarAppToUser(
        context: Context,
        car: Car,
        pairingCode: String,
        onSuccess: (DocumentSnapshot) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()
        val prefs = context.getSharedPreferences("car_config", Context.MODE_PRIVATE)

        db.collection("users")
            .whereEqualTo("pairingCode", pairingCode)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val userDoc = querySnapshot.documents.first()
                    val userId = userDoc.id

                    prefs.edit().putString("pairedUserId", userId).apply()

                    val carData = hashMapOf(
                        "carId" to car.carId,
                        "brand" to car.brand,
                        "type" to car.type,
                        "engine" to car.engine,
                        "fuelType" to car.fuelType,
                        "horsePower" to car.horsePower
                    )

                    db.collection("users")
                        .document(userId)
                        .collection("cars")
                        .document(car.carId!!)
                        .set(carData)
                        .addOnSuccessListener {
                            onSuccess(userDoc)
                        }
                        .addOnFailureListener { e ->
                            onFailure("Failed to write car data: ${e.localizedMessage}")
                        }



                } else {
                    onFailure("Invalid pairing code.")
                }
            }
            .addOnFailureListener { e ->
                onFailure("Database error: ${e.localizedMessage}")
            }
    }

    fun fetchCurrentUserCarData(
        onSuccess: (Car) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            onFailure("User not signed in")
            return
        }

        val db = FirebaseFirestore.getInstance()
        val carsCollection = db.collection("users")
            .document(currentUser.uid)
            .collection("cars")

        carsCollection.limit(1).get()
            .addOnSuccessListener { querySnapshot ->
                val document = querySnapshot.documents.firstOrNull()
                if (document != null) {
                    val car = document.toObject(Car::class.java)
                    if (car != null) {
                        onSuccess(car)
                    } else {
                        onFailure("Failed to parse car data.")
                    }
                } else {
                    onFailure("No car document found.")
                }
            }
            .addOnFailureListener {
                onFailure("Error fetching car: ${it.localizedMessage}")
            }
    }
}
