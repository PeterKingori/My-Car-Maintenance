package com.pkndegwa.mycarmaintenance.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pkndegwa.mycarmaintenance.data.model.Vehicle

@Database(entities = [Vehicle::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun vehicleDao(): VehicleDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "vehicle_app_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}