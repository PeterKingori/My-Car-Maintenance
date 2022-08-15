package com.pkndegwa.mycarmaintenance.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pkndegwa.mycarmaintenance.data.VehicleDao
import com.pkndegwa.mycarmaintenance.data.model.Vehicle
import kotlinx.coroutines.launch

/**
 * The [ViewModel] that is attached to the VehiclesFragment.
 */
class VehiclesViewModel(private val vehicleDao: VehicleDao) : ViewModel() {
    private fun insertVehicle(vehicle: Vehicle) {
        viewModelScope.launch {
            vehicleDao.insertVehicle(vehicle)
        }
    }

    private fun getNewVehicleEntry(vehicleType: String, vehicleManufacturer: String, vehicleModel: String,
    vehicleLicensePlate: String, vehicleFuelType: String, vehicleMileage: String): Vehicle {
        return Vehicle(
            type = vehicleType,
            manufacturer = vehicleManufacturer,
            model = vehicleModel,
            licensePlate = vehicleLicensePlate,
            fuelType = vehicleFuelType,
            mileage = vehicleMileage.toInt()
        )
    }

    fun addNewVehicle(vehicleType: String, vehicleManufacturer: String, vehicleModel: String,
                      vehicleLicensePlate: String, vehicleFuelType: String, vehicleMileage: String) {
        val newVehicle = getNewVehicleEntry(vehicleType, vehicleManufacturer, vehicleModel,
            vehicleLicensePlate, vehicleFuelType, vehicleMileage)
        insertVehicle(newVehicle)
    }

}