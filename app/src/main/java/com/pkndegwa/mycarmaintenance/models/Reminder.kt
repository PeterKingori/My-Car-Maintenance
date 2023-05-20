package com.pkndegwa.mycarmaintenance.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class Reminder(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "reminder_text")
    val reminderText: String,
    @ColumnInfo(name = "reminder_date")
    val reminderDate: String,
    @ColumnInfo(name = "additional_text")
    val additionalText: String = ""
)