package com.pkndegwa.mycarmaintenance.model

/**
 * Data model for registered vehicles.
 */
data class Vehicle(
    var type: String,
    var manufacturer: String,
    var model: String,
    var licensePlate: String,
    var modelYear: Int,
    var fuelType: String,
    var fuelCapacity: Int,
    var mileage: Int,
    var chassisNumber: String?,
)
