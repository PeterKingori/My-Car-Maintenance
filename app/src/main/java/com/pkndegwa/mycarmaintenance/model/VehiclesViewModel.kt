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

    private var _fuelType = ""
    val fuelType = _fuelType

    private var _mileage = 0
    val mileage = _mileage

    // The internal variable that stores a list of registered vehicles.
    private var _vehiclesData = ArrayList<Vehicle>()

    // The external immutable variable that's a backing field for _vehiclesData.
    val vehiclesData: ArrayList<Vehicle> get() = _vehiclesData

    init {
        // Initialise the list of vehicles if any exist.
        _vehiclesData = VehiclesData.getVehiclesData()
    }
}