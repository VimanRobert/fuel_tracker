package com.fueltracker.presentation.login

import android.content.Context
import androidx.lifecycle.ViewModel
import com.fueltracker.domain.GetUserDataUseCase
import com.fueltracker.domain.SetUserDataUseCase
import com.fueltracker.domain.model.Car
import com.fueltracker.domain.model.User
import com.fueltracker.presentation.utils.UserHelper
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val setUserDataUseCase: SetUserDataUseCase,
    private val getUserDataUseCase: GetUserDataUseCase
): ViewModel() {

    fun setNewUserProfile(
        userId: String,
        userEmail: String,
        userName: String,
        pairingCode: String,
        userCar: Car
    ) {
        setUserDataUseCase.setData(userId, userEmail, userName, pairingCode, userCar)
    }

    fun isUserConnected(context: Context, userHelper: UserHelper): Boolean {
        if (userHelper.getCurrentUser() != null) {
            userHelper.getUserToken(context)
            return true
        } else {
            return false
        }
    }
}