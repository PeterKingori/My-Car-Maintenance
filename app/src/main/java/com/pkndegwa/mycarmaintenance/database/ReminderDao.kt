package com.pkndegwa.mycarmaintenance.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pkndegwa.mycarmaintenance.models.Reminder
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: Reminder)

    @Update
    suspend fun updateReminder(reminder: Reminder)

    @Delete
    suspend fun deleteReminder(reminder: Reminder)

    @Query("SELECT * FROM reminders WHERE id = :id")
    fun getReminder(id: Int): Flow<Reminder>

    @Query("SELECT * FROM reminders ORDER BY reminder_date ASC")
    fun getAllReminders(): LiveData<List<Reminder>>
}