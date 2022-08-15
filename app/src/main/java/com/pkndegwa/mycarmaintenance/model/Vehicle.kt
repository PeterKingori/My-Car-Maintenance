package com.pkndegwa.mycarmaintenance.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data model for registered vehicles that is also used as the database entity class for the vehicles table.
 */
@Entity(tableName = "vehicles")
data class Vehicle(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "manufacturer")
    val manufacturer: String,
    @ColumnInfo(name = "model")
    val model: String,
    @ColumnInfo(name = "license_plate")
    val licensePlate: String,
    @ColumnInfo(name = "fuel_type")
    val fuelType: String,
    @ColumnInfo(name = "mileage")
    val mileage: Int
)
