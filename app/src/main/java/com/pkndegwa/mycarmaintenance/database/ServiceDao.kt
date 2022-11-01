package com.pkndegwa.mycarmaintenance.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pkndegwa.mycarmaintenance.models.Service
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addService(service: Service)

    @Update
    suspend fun updateService(service: Service)

    @Delete
    suspend fun deleteService(service: Service)

    @Query("SELECT * FROM services WHERE id = :id")
    fun getService(id: Int): Flow<Service>

    @Query("SELECT * FROM services WHERE vehicle_id = :vehicleId ORDER BY service_date DESC")
    fun getAllServicesByVehicle(vehicleId: Int): LiveData<List<Service>>
}