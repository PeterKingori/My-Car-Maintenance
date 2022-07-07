package com.pkndegwa.mycarmaintenance.model

import androidx.lifecycle.ViewModel
import com.pkndegwa.mycarmaintenance.data.VehiclesData

/**
 * The [ViewModel] that is attached to the VehiclesFragment.
 */
class VehiclesViewModel : ViewModel() {
    // The internal variable that stores a list of registered vehicles.
    private var _vehiclesData = ArrayList<Vehicle>()

    // The external immutable variable that's a backing field for _vehiclesData.
    val vehiclesData: ArrayList<Vehicle> get() = _vehiclesData

    init {
        // Initialise the list of vehicles if any exist.
        _vehiclesData = VehiclesData.vehicles
    }
}