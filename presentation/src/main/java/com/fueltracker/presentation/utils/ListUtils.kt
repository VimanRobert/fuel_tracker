package com.fueltracker.presentation.utils

import com.fueltracker.domain.model.HomeDestination
import com.fueltracker.domain.model.HomeItem

val homeItemsList = listOf(
    HomeItem("Info", HomeDestination.INFO),
    HomeItem("User Status", HomeDestination.USER_STATUS),
    HomeItem("Car Status", HomeDestination.CAR_STATUS),
    HomeItem("General Reports", HomeDestination.GENERAL_REPORTS),
    HomeItem("Daily Reports", HomeDestination.DAILY_REPORTS),
    HomeItem("Fuel Info", HomeDestination.FUEL_INFO),
)