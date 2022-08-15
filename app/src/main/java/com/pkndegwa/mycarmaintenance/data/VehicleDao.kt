package com.pkndegwa.mycarmaintenance.data

import androidx.room.*
import com.pkndegwa.mycarmaintenance.data.model.Vehicle
import kotlinx.coroutines.flow.Flow

@Dao
interface VehicleDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertVehicle(vehicle: Vehicle)

    @Update
    suspend fun updateVehicle(vehicle: Vehicle)

    @Delete
    suspend fun deleteVehicle(vehicle: Vehicle)

    @Query("SELECT * FROM vehicles WHERE id = :id")
    fun getVehicle(id: Int): Flow<Vehicle>

    @Query("SELECT * FROM vehicles ORDER BY manufacturer ASC")
    fun getAllVehicles(): Flow<List<Vehicle>>
}