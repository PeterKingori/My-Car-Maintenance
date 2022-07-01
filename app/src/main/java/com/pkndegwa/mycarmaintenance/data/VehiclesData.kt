package com.pkndegwa.mycarmaintenance.data

import com.pkndegwa.mycarmaintenance.model.Vehicle

object VehiclesData {
    val vehicles = mutableListOf<Vehicle>()

    fun getVehiclesData(): List<Vehicle> {
        return vehicles
    }
}