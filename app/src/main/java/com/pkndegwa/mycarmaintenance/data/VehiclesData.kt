package com.pkndegwa.mycarmaintenance.data

import com.pkndegwa.mycarmaintenance.model.Vehicle

object VehiclesData {
    val vehicles = ArrayList<Vehicle>()

    init {
        initialiseVehicles()
    }

    private fun initialiseVehicles() {
        var vehicle = Vehicle("Car", "Suzuki", "Swift", "KAE 23", "Petrol", 12)
        vehicles.add(vehicle)

        vehicle = Vehicle("Car", "Fiat", "500s", "BAE3rf", "Petrol", 10000)
        vehicles.add(vehicle)

        vehicle = Vehicle("Motorcycle", "Kawasaki", "Vulcan 900", "MH183J", "Petrol", 532)
        vehicles.add(vehicle)

        vehicle = Vehicle("Truck", "Mercedes", "Scania", "KAF 111S", "Diesel", 0)
        vehicles.add(vehicle)
    }

}