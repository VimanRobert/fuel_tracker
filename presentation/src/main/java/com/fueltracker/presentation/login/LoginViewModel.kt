package com.fueltracker.presentation.login

import com.fueltracker.domain.GetUserDataUseCase
import com.fueltracker.domain.SetUserDataUseCase
import com.fueltracker.domain.model.Car
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

class LoginViewModel(
    private val setUserDataUseCase: SetUserDataUseCase
){
    fun setNewUserProfile(
        userId: String,
        userEmail: String,
        userCar: Car
    ) {
        setUserDataUseCase.setData(userId, userEmail, userCar)
    }
}