package com.pkndegwa.mycarmaintenance.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.material.chip.ChipGroup
import com.pkndegwa.mycarmaintenance.database.ServiceDao
import com.pkndegwa.mycarmaintenance.models.Service
import kotlinx.coroutines.launch

class ServicesViewModel(private val serviceDao: ServiceDao) : ViewModel() {

    fun getAllServicesForVehicle(vehicleId: Int) = serviceDao.getAllServicesByVehicle(vehicleId)

    /**
     * This method adds a [Service] object to the database on a background thread.
     * @param [service]
     */
    private fun insertService(service: Service): Boolean {
        return try {
            viewModelScope.launch { serviceDao.insertService(service) }
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Converts service details that have been entered to a new [Service] instance and returns it.
     * @return Service
     */
    private fun createNewServiceEntry(
        servicesList: String, currentMileage: String, nextServiceMileage: String, totalCost: String,
        serviceDate: String, nextServiceDate: String, notes: String, vehicleId: Int
    ): Service {
        return Service(
            servicesDoneList = servicesList,
            currentMileage = currentMileage.toInt(),
            nextServiceMileage = nextServiceMileage.toInt(),
            totalCost = totalCost.toDouble(),
            serviceDate = serviceDate,
            nextServiceDate = nextServiceDate,
            notes = notes,
            vehicleId = vehicleId
        )
    }

    /**
     * Public function that takes in vehicle service details, gets
     */
    fun addNewService(
        servicesList: String, currentMileage: String, nextServiceMileage: String, totalCost: String,
        serviceDate: String, nextServiceDate: String, notes: String, vehicleId: Int
    ): Boolean {
        val newService = createNewServiceEntry(
            servicesList, currentMileage, nextServiceMileage, totalCost, serviceDate,
            nextServiceDate, notes, vehicleId
        )
        return insertService(newService)
    }

    /**
     * Public function that checks that chips have been selected
     */
    fun isServiceSelected(chipGroup: ChipGroup): Boolean {
        return chipGroup.checkedChipIds != emptyList<String>()
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

    /**
     * This function retrieves the service details from the database based on the service [id].
     * @return LiveData<Service>
     */
    fun retrieveService(id: Int?): LiveData<Service>? {
        return id?.let { serviceDao.getService(it).asLiveData() }
    }

    /**
     * This function deletes a Service object from the database on a background thread.
     */
    fun deleteService(service: Service) {
        viewModelScope.launch { serviceDao.deleteService(service) }
    }

    /**
     * This function takes in a [Service] object and updates the data of the existing
     * service in the database on a background thread.
     * @param [service]
     */
    private fun update(service: Service) {
        viewModelScope.launch { serviceDao.updateService(service) }
    }

    /**
     * Converts edited service details that have been entered by the user to a [Service] instance
     * and returns it.
     * @return Service
     */
    private fun getUpdatedServiceEntry(
        serviceId: Int, servicesList: String, currentMileage: String, nextServiceMileage: String,
        totalCost: String, serviceDate: String, nextServiceDate: String, notes: String, vehicleId: Int
    ): Service {
        return Service(
            id = serviceId,
            servicesDoneList = servicesList,
            currentMileage = currentMileage.toInt(),
            nextServiceMileage = nextServiceMileage.toInt(),
            totalCost = totalCost.toDouble(),
            serviceDate = serviceDate,
            nextServiceDate = nextServiceDate,
            notes = notes,
            vehicleId = vehicleId
        )
    }

    /**
     * Public function that takes in updated service details, gets an updated [Service] instance,
     * and passes the information to [update] to be updated in the database.
     */
    fun updateService(
        serviceId: Int, servicesList: String, currentMileage: String, nextServiceMileage: String,
        totalCost: String, serviceDate: String, nextServiceDate: String, notes: String, vehicleId: Int
    ) {
        val updatedService = getUpdatedServiceEntry(
            serviceId, servicesList, currentMileage, nextServiceMileage, totalCost,
            serviceDate, nextServiceDate, notes, vehicleId
        )
        update(updatedService)
    }
}