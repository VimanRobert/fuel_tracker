package com.fueltracker.presentation.userstatus

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.fueltracker.domain.GetUserDataUseCase
import com.fueltracker.presentation.utils.UserHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserStatusViewModel @Inject constructor(
    private val getUserDataUseCase: GetUserDataUseCase,
//    private val userHelper: UserHelper
) : ViewModel() {

//    val userEmail: LiveData<String?> = liveData {
//        emit(userHelper.getGoogleEmail())
//    }
}
