package com.pkndegwa.mycarmaintenance.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pkndegwa.mycarmaintenance.models.Vehicle
import kotlinx.coroutines.flow.Flow

@Dao
interface VehicleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVehicle(vehicle: Vehicle)

    @Update
    suspend fun updateVehicle(vehicle: Vehicle)

    @Delete
    suspend fun deleteVehicle(vehicle: Vehicle)

    @Query("SELECT * FROM vehicles WHERE id = :id")
    fun getVehicle(id: Int): Flow<Vehicle>

    @Query("SELECT * FROM vehicles ORDER BY manufacturer ASC")
    fun getAllVehicles(): LiveData<List<Vehicle>>
}