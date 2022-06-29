package com.pkndegwa.mycarmaintenance.ui

import androidx.lifecycle.ViewModel
import com.pkndegwa.mycarmaintenance.data.ManufacturerData
import com.pkndegwa.mycarmaintenance.model.Manufacturer

class ManufacturerViewModel: ViewModel() {
    private var _manufacturerData: List<Manufacturer> = listOf()
    val manufacturerData: List<Manufacturer> get() = _manufacturerData

    init {
        // Initialise the manufacturer data.
        _manufacturerData = ManufacturerData.getManufacturerData()
    }
}