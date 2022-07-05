package com.pkndegwa.mycarmaintenance.data

import com.pkndegwa.mycarmaintenance.model.Vehicle

object VehiclesData {
    val vehicles = ArrayList<Vehicle>()

    fun getVehiclesData(): ArrayList<Vehicle> {
        return vehicles
    }
}