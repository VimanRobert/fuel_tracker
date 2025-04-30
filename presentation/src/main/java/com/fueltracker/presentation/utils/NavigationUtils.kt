package com.fueltracker.presentation.utils

import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.NavHostFragment
import com.fueltracker.presentation.R

fun FragmentActivity.handleBackButtonNavigation() {
    val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as? NavHostFragment
    val navController = navHostFragment?.navController

    if (navController?.currentDestination?.id == R.id.homeFragment) {
        finishAffinity()
    } else {
        navController?.popBackStack()
    }
}
