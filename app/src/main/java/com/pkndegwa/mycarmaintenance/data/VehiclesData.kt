package com.pkndegwa.mycarmaintenance.data

import com.pkndegwa.mycarmaintenance.model.Vehicle

object VehiclesData {
    val vehicles = mutableListOf<Vehicle>()

    fun getVehiclesData(): List<Vehicle> {
        vehicles.add(
            Vehicle(
                type = "car",
                manufacturer = "Mercedes-Benz",
                model = "AMG A45S",
                licensePlate = "KCX 583R",
                modelYear = 2021,
                fuelType = "Petrol",
                fuelCapacity = 3,
                mileage = 0,
                chassisNumber = ""
            )
        )
        return vehicles
    }
}