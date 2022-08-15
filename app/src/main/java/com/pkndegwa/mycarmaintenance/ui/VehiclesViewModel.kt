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

    /**
     * This function takes in a [Vehicle] object and adds the data to the database
     * on a background thread.
     * @param [vehicle]
     */
    private fun insertVehicle(vehicle: Vehicle) {
        viewModelScope.launch {
            vehicleDao.insertVehicle(vehicle)
        }
    }

    /**
     * Converts vehicle details that have been entered by the user to a new [Vehicle] instance
     * and returns it.
     * @return Vehicle
     */
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

    /**
     * Public function that takes in vehicle details, gets a new [Vehicle] instance,
     * and passes the information to [insertVehicle] to be saved to the database.
     */
    fun addNewVehicle(vehicleType: String, vehicleManufacturer: String, vehicleModel: String,
                      vehicleLicensePlate: String, vehicleFuelType: String, vehicleMileage: String) {
        val newVehicle = getNewVehicleEntry(vehicleType, vehicleManufacturer, vehicleModel,
            vehicleLicensePlate, vehicleFuelType, vehicleMileage)
        insertVehicle(newVehicle)
    }

    /**
     * Public function that checks if a string value is blank or not.
     */
    fun isEntryValid(propertyValue: String): Boolean {
        if (propertyValue.isBlank()) {
            return false
        }
        return true
    }

}