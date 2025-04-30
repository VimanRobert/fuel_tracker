package com.fueltracker.domain.model

data class User(
    val userId: String = "",
    val userEmail: String = "",
    val userName: String = "",
    val pairingCode: String = "",
    val carCategory: Car? = null
)
