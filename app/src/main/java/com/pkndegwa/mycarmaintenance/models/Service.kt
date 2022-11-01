package com.pkndegwa.mycarmaintenance.models

import androidx.room.Entity

/**
 * Data model for service done that is also used as the database entity class for the services table.
 */
@Entity(tableName = "services")
data class Service(
    val servicesDoneList: MutableList<String>,
    val currentMileage: Int,
    val nextServiceMileage: Int,
    val totalCost: Double,
    val serviceDate: String,
    val nextServiceDate: String = "",
    val notes: String = ""
)
