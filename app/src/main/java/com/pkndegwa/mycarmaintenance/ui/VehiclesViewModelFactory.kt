package com.pkndegwa.mycarmaintenance.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pkndegwa.mycarmaintenance.data.VehicleDao

class VehiclesViewModelFactory(private val vehicleDao: VehicleDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VehiclesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VehiclesViewModel(vehicleDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}