package com.pkndegwa.mycarmaintenance.model

/**
 * Data model for registered vehicles.
 */
data class Vehicle(
    val type: String,
    val manufacturer: String,
    val model: String,
    val licensePlate: String,
    val fuelType: String,
    val mileage: Int,
)
