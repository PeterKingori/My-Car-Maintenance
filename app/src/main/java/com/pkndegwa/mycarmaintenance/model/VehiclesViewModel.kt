package com.pkndegwa.mycarmaintenance.model

import androidx.lifecycle.ViewModel
import com.pkndegwa.mycarmaintenance.data.VehiclesData

/**
 * The [ViewModel] that is attached to the VehiclesFragment.
 */
class VehiclesViewModel : ViewModel() {
    private var _vehicleType = ""
    val vehicleType = _vehicleType

    private var _manufacturer = ""
    val manufacturer = _manufacturer

    private var _model = ""
    val model = _model

    private var _licensePlate = ""
    val licensePlate = _licensePlate

    private var _modelYear = 1900
    val modelYear = _modelYear

    private var _fuelType = ""
    val fuelType = _fuelType

    private var _fuelCapacity = 0.0
    val fuelCapacity = _fuelCapacity

    private var _mileage = 0
    val mileage = _mileage

    private var _chassisNumber = ""
    val chassisNumber = _chassisNumber

    // The internal variable that stores a list of registered vehicles.
    private var _vehiclesData: List<Vehicle>? = listOf()

    // The external immutable variable that's a backing field for _vehiclesData.
    val vehiclesData: List<Vehicle>? get() = _vehiclesData

    init {
        // Initialise the list of vehicles if any exist.
        _vehiclesData = VehiclesData.getVehiclesData()
    }

    // Set vehicle details.
    fun setVehicleDetails(
        type: String, vehicleManufacturer: String, vehicleModel: String, vehicleLicensePlate: String,
        vehicleModelYear: Int, vehicleFuelType: String, vehicleFuelCapacity: Double, vehicleMileage:
        Int, vehicleChassisNumber: String = ""
    ) {
        _vehicleType = type
        _manufacturer = vehicleManufacturer
        _model = vehicleModel
        _licensePlate = vehicleLicensePlate
        _modelYear = vehicleModelYear
        _fuelType = vehicleFuelType
        _fuelCapacity = vehicleFuelCapacity
        _mileage = vehicleMileage
        _chassisNumber = vehicleChassisNumber

        val vehicle = Vehicle(
            type, vehicleManufacturer, vehicleModel, vehicleLicensePlate,
            vehicleModelYear, vehicleFuelType, vehicleFuelCapacity, vehicleMileage, vehicleChassisNumber
        )
        VehiclesData.vehicles.add(vehicle)
    }
}