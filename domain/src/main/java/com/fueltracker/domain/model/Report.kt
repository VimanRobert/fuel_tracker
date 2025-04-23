package com.fueltracker.domain.model

import java.util.Date

data class Report(
    val fuelUsed: String,
    val distance: String,
    val date: Date
)
