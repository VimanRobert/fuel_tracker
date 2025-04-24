package com.fueltracker.presentation.userstatus

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.fueltracker.presentation.utils.UserHelper

class UserStatusViewModel(
    private val userHelper: UserHelper
) : ViewModel() {

    val userEmail: LiveData<String?> = liveData {
        emit(userHelper.getGoogleEmail())
    }
}
