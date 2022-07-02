package com.pkndegwa.mycarmaintenance.data

import com.pkndegwa.mycarmaintenance.model.Vehicle

object VehiclesData {
    val vehicles = arrayListOf<Vehicle>()

    fun getVehiclesData(): List<Vehicle> {
        return vehicles
    }
}