package com.pkndegwa.mycarmaintenance.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicles")
data class Vehicle(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "vehicle_image")
    val vehicleImageUri: String,
    @ColumnInfo(name = "type")
    val type: String,
    @ColumnInfo(name = "manufacturer")
    val manufacturer: String,
    @ColumnInfo(name = "model")
    val model: String,
    @ColumnInfo(name = "model_year")
    val modelYear: Int,
    @ColumnInfo(name = "license_plate")
    val licensePlate: String,
    @ColumnInfo(name = "fuel_type")
    val fuelType: String,
    @ColumnInfo(name = "mileage")
    val mileage: Int
)
