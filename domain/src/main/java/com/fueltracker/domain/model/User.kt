package com.fueltracker.domain.model

data class User(
    val userId: String,
    val userEmail: String,
    val carCategory: Car?
)
