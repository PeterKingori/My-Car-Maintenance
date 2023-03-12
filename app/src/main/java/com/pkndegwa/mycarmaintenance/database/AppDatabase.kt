package com.pkndegwa.mycarmaintenance.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pkndegwa.mycarmaintenance.models.Note
import com.pkndegwa.mycarmaintenance.models.Reminder
import com.pkndegwa.mycarmaintenance.models.Service
import com.pkndegwa.mycarmaintenance.models.Vehicle

@Database(
    entities = [Vehicle::class, Service::class, Reminder::class, Note::class], version = 8, exportSchema =
    true, autoMigrations = [AutoMigration(from = 7, to = 8)]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun vehicleDao(): VehicleDao
    abstract fun serviceDao(): ServiceDao
    abstract fun reminderDao(): ReminderDao
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "vehicle_app_database"
                ).fallbackToDestructiveMigrationOnDowngrade()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}