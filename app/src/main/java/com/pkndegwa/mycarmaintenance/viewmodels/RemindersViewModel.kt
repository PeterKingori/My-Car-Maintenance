package com.pkndegwa.mycarmaintenance.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.pkndegwa.mycarmaintenance.database.ReminderDao
import com.pkndegwa.mycarmaintenance.models.Reminder
import kotlinx.coroutines.launch

/**
 * The [ViewModel] that is attached to the ReminderFragment.
 */
class RemindersViewModel(private val reminderDao: ReminderDao) : ViewModel() {

    fun getAllReminders() = reminderDao.getAllReminders()

    /**
     * This method adds a [Reminder] object to the database on a background thread.
     * @param [reminder]
     * @return Boolean
     */
    private fun insertReminder(reminder: Reminder): Boolean {
        return try {
            viewModelScope.launch { reminderDao.insertReminder(reminder) }
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Converts reminder details that have been entered by a user to a new [Reminder] instance and returns it.
     * @return Reminder
     */
    private fun createNewReminderEntry(reminderText: String, reminderDate: String, additionalText: String): Reminder {
        return Reminder(
            reminderText = reminderText,
            reminderDate = reminderDate,
            additionalText = additionalText
        )
    }

    /**
     * Public function that takes in reminder data, gets a new [Reminder] instance,
     * and passes the information to [insertReminder] to be saved to the database.
     * @return Boolean
     */
    fun addNewReminder(reminderText: String, reminderDate: String, additionalText: String): Boolean {
        val newReminder = createNewReminderEntry(reminderText, reminderDate, additionalText)
        return insertReminder(newReminder)
    }

    /**
     * This function retrieves the reminder details from the database based on the [id].
     * @return LiveData<Reminder>
     */
    fun retrieveReminder(id: Int): LiveData<Reminder> {
        return reminderDao.getReminder(id).asLiveData()
    }

    /**
     * This function deletes a [Reminder] object from the database on a background thread.
     */
    fun deleteReminder(reminder: Reminder) {
        viewModelScope.launch {
            reminderDao.deleteReminder(reminder)
        }
    }

    /**
     * This function takes in a [Reminder] object and updates the data of the existing
     * reminder in the database on a background thread.
     * @param [reminder]
     * @return Boolean
     */
    private fun updateReminder(reminder: Reminder): Boolean {
        return try {
            viewModelScope.launch { reminderDao.updateReminder(reminder) }
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Converts edited reminder details that have been entered by the user to a [Reminder] instance
     * and returns it.
     * @return Reminder
     */
    private fun getUpdatedReminderEntry(
        reminderId: Int, reminderText: String, reminderDate: String, additionalText: String
    ): Reminder {
        return Reminder(
            id = reminderId,
            reminderText = reminderText,
            reminderDate = reminderDate,
            additionalText = additionalText
        )
    }

    /**
     * Public function that takes in updated reminder details, gets an updated [Reminder] instance,
     * and passes the information to [updateReminder] to be updated in the database.
     */
    fun updateReminder(reminderId: Int, reminderText: String, reminderDate: String, additionalText: String): Boolean {
        val updatedReminder = getUpdatedReminderEntry(reminderId, reminderText, reminderDate, additionalText)
        return updateReminder(updatedReminder)
    }
}