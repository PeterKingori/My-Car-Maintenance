package com.pkndegwa.mycarmaintenance.model

import androidx.lifecycle.ViewModel
import com.pkndegwa.mycarmaintenance.data.ManufacturerData

/**
 * The [ViewModel] that is attached to the VehicleManufacturersFragment.
 */
class ManufacturerViewModel : ViewModel() {
    // The internal variable that stores a list of vehicle manufacturers.
    private var _manufacturerData: List<Manufacturer> = listOf()

    // The external immutable variable that's a backing field for _manufacturerData.
    val manufacturerData: List<Manufacturer> get() = _manufacturerData

    init {
        // Initialise the manufacturer data.
        _manufacturerData = ManufacturerData.getManufacturerData()
    }
}