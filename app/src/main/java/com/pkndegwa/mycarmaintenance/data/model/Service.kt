package com.pkndegwa.mycarmaintenance.data.model

/**
 * Data model for service done that is also used as the database entity class for the services table.
 */
data class Service(
    val servicesDoneList: MutableList<String>,
    val currentMileage: Int,
    val nextServiceMileage: Int,
    val totalCost: Double,
    val serviceDate: String,
    val nextServiceDate: String = "",
    val notes: String = ""
)
