package com.pkndegwa.mycarmaintenance.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pkndegwa.mycarmaintenance.database.ServiceDao

class ServicesViewModelFactory(private val serviceDao: ServiceDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ServicesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ServicesViewModel(serviceDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}