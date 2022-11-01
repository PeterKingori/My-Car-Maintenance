package com.pkndegwa.mycarmaintenance

import android.app.Application
import com.pkndegwa.mycarmaintenance.database.AppDatabase

class CarMaintenanceApplication: Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}