package com.pkndegwa.mycarmaintenance.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "services")
data class Service(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "services_done_list")
    val servicesDoneList: String,
    @ColumnInfo(name = "current_mileage")
    val currentMileage: Int,
    @ColumnInfo(name = "next_service_mileage")
    val nextServiceMileage: Int,
    @ColumnInfo(name = "total_cost")
    val totalCost: Double,
    @ColumnInfo(name = "service_date")
    val serviceDate: String,
    @ColumnInfo(name = "next_service_date")
    val nextServiceDate: String = "",
    @ColumnInfo(name = "notes")
    val notes: String = "",
    @ColumnInfo(name = "receipt_image")
    val receiptImageUriString: String? = null,
    @ColumnInfo(name = "vehicle_id")
    val vehicleId: Int
)
