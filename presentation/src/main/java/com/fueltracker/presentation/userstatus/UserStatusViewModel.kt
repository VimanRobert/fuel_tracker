package com.fueltracker.presentation.userstatus

import android.content.Context
import androidx.lifecycle.ViewModel
import com.fueltracker.domain.GetUserDataUseCase
import com.fueltracker.domain.SetCarDataUseCase
import com.fueltracker.domain.SetUserDataUseCase
import com.fueltracker.domain.model.Car
import com.fueltracker.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserStatusViewModel @Inject constructor(
    private val setUserDataUseCase: SetUserDataUseCase,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val setCarDataUseCase: SetCarDataUseCase
) : ViewModel() {

    suspend fun fetchCurrentUserData(): User? {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        return firebaseUser?.uid?.let {
            getUserDataUseCase.getData(it)
        }
    }

    suspend fun retrieveUserData(userId: String): User? {
        return getUserDataUseCase.getData(userId)
    }

    fun getUserIdByPairingCode(pairingCode: String) {
        val db = FirebaseFirestore.getInstance()

        db.collection("users")
            .whereEqualTo("pairingCode", pairingCode)
            .get()
    }

    fun callCarData(context: Context): Car {
        return setCarDataUseCase.carDataGenerator(context)
    }
}
