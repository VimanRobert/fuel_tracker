package com.fueltracker.presentation.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.fueltracker.domain.GetCarDataUseCase
import com.fueltracker.domain.model.Car
import com.fueltracker.presentation.utils.ChartsHandler.createLinearChart
import com.fueltracker.presentation.utils.ChartsHandler.setupChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.security.KeyStore
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

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

    fun fetchUserCarDailyReports(
        userId: String,
        onResult: (entries: List<Entry>, timeLabels: List<String>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()
        val now = Instant.now()
        val cutoff = now.minus(Duration.ofHours(24))

        val rawList = mutableListOf<Triple<Instant, Float, String>>() // time, avg, formatted time string

        db.collection("users")
            .document(userId)
            .collection("reports")
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    val isoDate = doc.getString("date") ?: continue
                    val avgStr = doc.getString("mediumConsumptionLevel") ?: continue
                    val avg = avgStr.toFloatOrNull() ?: continue

                    val instant = try {
                        Instant.parse(isoDate)
                    } catch (e: Exception) {
                        continue
                    }

                    if (instant.isAfter(cutoff)) {
                        val label = instant.atZone(ZoneId.systemDefault())
                            .format(DateTimeFormatter.ofPattern("HH:mm"))

                        rawList.add(Triple(instant, avg, label))
                    }
                }

                val sorted = rawList.sortedBy { it.first }

                val entries = sorted.mapIndexed { index, triple ->
                    Entry(index.toFloat(), triple.second)
                }

                val labels = sorted.map { it.third }

                onResult(entries, labels)
            }
            .addOnFailureListener { onError(it) }
    }
}
