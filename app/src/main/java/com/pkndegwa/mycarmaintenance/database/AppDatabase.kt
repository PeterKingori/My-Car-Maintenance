package com.pkndegwa.mycarmaintenance.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pkndegwa.mycarmaintenance.models.Service
import com.pkndegwa.mycarmaintenance.models.Vehicle
import com.pkndegwa.mycarmaintenance.utils.Converters

@Database(entities = [Vehicle::class, Service::class], version = 3)
abstract class AppDatabase: RoomDatabase() {
    abstract fun vehicleDao(): VehicleDao
    abstract fun serviceDao(): ServiceDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "vehicle_app_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}