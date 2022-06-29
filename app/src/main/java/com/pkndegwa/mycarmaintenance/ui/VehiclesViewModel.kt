package com.pkndegwa.mycarmaintenance.ui

import androidx.lifecycle.ViewModel
import com.pkndegwa.mycarmaintenance.data.VehiclesData
import com.pkndegwa.mycarmaintenance.model.Vehicle

/**
 * The [ViewModel] that is attached to the [VehiclesFragment].
 */
class VehiclesViewModel : ViewModel() {
    // The internal variable that stores a list of registered vehicles.
    private var _vehiclesData: List<Vehicle>? = listOf()

    // The external immutable variable that's a backing field for _vehiclesData.
    val vehiclesData: List<Vehicle>?
        get() = _vehiclesData

    init {
        // Initialise the list of vehicles if any exist.
        _vehiclesData = VehiclesData.getVehiclesData()
    }
}