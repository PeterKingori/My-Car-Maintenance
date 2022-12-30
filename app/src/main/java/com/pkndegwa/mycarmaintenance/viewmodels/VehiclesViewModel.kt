package com.pkndegwa.mycarmaintenance.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.pkndegwa.mycarmaintenance.database.VehicleDao
import com.pkndegwa.mycarmaintenance.models.Vehicle
import kotlinx.coroutines.launch

/**
 * The [ViewModel] that is attached to the VehiclesFragment.
 */
class VehiclesViewModel(private val vehicleDao: VehicleDao) : ViewModel() {

    fun getAllVehicles() = vehicleDao.getAllVehicles()

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
    private fun createNewVehicleEntry(
        vehicleImageUri: String, vehicleType: String, vehicleManufacturer: String, vehicleModel: String,
        vehicleModelYear: String, vehicleLicensePlate: String, vehicleFuelType: String, vehicleMileage: String
    ): Vehicle {
        return Vehicle(
            vehicleImageUri = vehicleImageUri,
            type = vehicleType,
            manufacturer = vehicleManufacturer,
            model = vehicleModel,
            modelYear = vehicleModelYear.toInt(),
            licensePlate = vehicleLicensePlate,
            fuelType = vehicleFuelType,
            mileage = vehicleMileage.toInt()
        )
    }

    /**
     * Public function that takes in vehicle details, gets a new [Vehicle] instance,
     * and passes the information to [insertVehicle] to be saved to the database.
     */
    fun addNewVehicle(
        vehicleImageUri: String, vehicleType: String, vehicleManufacturer: String, vehicleModel: String,
        vehicleModelYear: String, vehicleLicensePlate: String, vehicleFuelType: String, vehicleMileage: String
    ) {
        val newVehicle = createNewVehicleEntry(
            vehicleImageUri, vehicleType, vehicleManufacturer,
            vehicleModel, vehicleModelYear, vehicleLicensePlate, vehicleFuelType, vehicleMileage
        )
        insertVehicle(newVehicle)
    }

    /**
     * This function retrieves the vehicle details from the database based on the vehicle [id].
     * @return LiveData<Vehicle>
     */
    fun retrieveVehicle(id: Int?): LiveData<Vehicle>? {
        return id?.let { vehicleDao.getVehicle(it).asLiveData() }
    }

    /**
     * This function deletes a Vehicle object from the database on a background thread.
     */
    fun deleteVehicle(vehicle: Vehicle) {
        viewModelScope.launch {
            vehicleDao.deleteVehicle(vehicle)
        }
    }

    /**
     * This function takes in a [Vehicle] object and updates the data of the existing
     * vehicle in the database on a background thread.
     * @param [vehicle]
     * @return Boolean
     */
    private fun update(vehicle: Vehicle): Boolean {
        return try {
            viewModelScope.launch { vehicleDao.updateVehicle(vehicle) }
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Converts edited vehicle details that have been entered by the user to a [Vehicle] instance
     * and returns it.
     * @return Vehicle
     */
    private fun getUpdatedVehicleEntry(
        vehicleId: Int, vehicleImageUri: String, vehicleType: String, vehicleManufacturer: String, vehicleModel:
        String, vehicleModelYear: String, vehicleLicensePlate: String, vehicleFuelType: String, vehicleMileage: String
    ): Vehicle {
        return Vehicle(
            id = vehicleId,
            vehicleImageUri = vehicleImageUri,
            type = vehicleType,
            manufacturer = vehicleManufacturer,
            model = vehicleModel,
            modelYear = vehicleModelYear.toInt(),
            licensePlate = vehicleLicensePlate,
            fuelType = vehicleFuelType,
            mileage = vehicleMileage.toInt()
        )
    }

    /**
     * Public function that takes in updated vehicle details, gets an updated [Vehicle] instance,
     * and passes the information to [update] to be updated in the database.
     */
    fun updateVehicle(
        vehicleId: Int,
        vehicleImageUri: String,
        vehicleType: String,
        vehicleManufacturer: String,
        vehicleModel: String,
        vehicleModelYear: String,
        vehicleLicensePlate: String,
        vehicleFuelType: String,
        vehicleMileage: String
    ): Boolean {
        val updatedVehicle = getUpdatedVehicleEntry(
            vehicleId, vehicleImageUri, vehicleType, vehicleManufacturer, vehicleModel, vehicleModelYear,
            vehicleLicensePlate, vehicleFuelType, vehicleMileage
        )
        return update(updatedVehicle)
    }
}