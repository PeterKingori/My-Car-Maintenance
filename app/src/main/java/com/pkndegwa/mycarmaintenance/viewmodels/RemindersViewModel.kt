package com.pkndegwa.mycarmaintenance.viewmodels

import android.app.AlarmManager
import android.app.Application
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.os.SystemClock
import androidx.core.app.AlarmManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import com.pkndegwa.mycarmaintenance.database.ReminderDao
import com.pkndegwa.mycarmaintenance.models.Reminder
import com.pkndegwa.mycarmaintenance.receiver.AlarmReceiver
import com.pkndegwa.mycarmaintenance.utils.cancelNotifications
import kotlinx.coroutines.launch

/**
 * The [ViewModel] that is attached to the ReminderFragment.
 */
class RemindersViewModel(private val app: Application, private val reminderDao: ReminderDao) : ViewModel() {
    private var reminderMessage = ""

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
        reminderMessage = reminderText
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
     * @return Boolean
     */
    fun updateReminder(reminderId: Int, reminderText: String, reminderDate: String, additionalText: String): Boolean {
        reminderMessage = reminderText
        val updatedReminder = getUpdatedReminderEntry(reminderId, reminderText, reminderDate, additionalText)
        return updateReminder(updatedReminder)
    }

    /*
   Section to add a reminder as a notification
    */
    private val REQUEST_CODE = 0

    private val second: Long = 1_000L
    private var triggerTime = 0L

    private var notifyPendingIntent: PendingIntent
    private val alarmManager = app.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val notifyIntent = Intent(app, AlarmReceiver::class.java)

    private val _dateSelection = MutableLiveData<Long>()
    private val dateSelection: LiveData<Long> get() = _dateSelection

    private val _elapsedTime = MutableLiveData<Long>()

    private var _alarmOn = MutableLiveData<Boolean>()

    private lateinit var timer: CountDownTimer

    init {
        _alarmOn.value = PendingIntent.getBroadcast(
            app,
            REQUEST_CODE,
            notifyIntent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        ) != null

        notifyPendingIntent = PendingIntent.getBroadcast(
            app,
            REQUEST_CODE,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // If alarm is not null resume the timer back for this alarm
        if (_alarmOn.value!!) {
            createTimer()
        }
    }

    /**
     * Turns the alarm on or off
     * @param isChecked, alarm status to be set
     */
    fun setAlarm(isChecked: Boolean) {
        when (isChecked) {
            true -> dateSelection.value?.let { startTimer(it) }
            false -> cancelNotification()
        }
    }

    /**
     * Creates a new alarm, notification and timer
     */
    private fun startTimer(dateSelected: Long) {
        _alarmOn.value?.let {
            if (!it) {
                _alarmOn.value = true
                val timeToDate = dateSelected - System.currentTimeMillis()
                val triggerTime = SystemClock.elapsedRealtime() + timeToDate

                val notificationManager = ContextCompat.getSystemService(
                    app,
                    NotificationManager::class.java
                ) as NotificationManager
                notificationManager.cancelNotifications()

                notifyIntent.putExtra("reminder_body", reminderMessage)
                notifyPendingIntent = PendingIntent.getBroadcast(
                    app,
                    REQUEST_CODE,
                    notifyIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                AlarmManagerCompat.setExact(
                    alarmManager,
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    triggerTime,
                    notifyPendingIntent
                )
            }
        }
        createTimer()
    }

    /**
     * Creates a new timer
     */
    private fun createTimer() {
        viewModelScope.launch {
            timer = object : CountDownTimer(triggerTime, second) {
                override fun onTick(millisUntilFinished: Long) {
                    _elapsedTime.value = triggerTime - SystemClock.elapsedRealtime()
                    if (_elapsedTime.value!! <= 0) {
                        resetTimer()
                    }
                }

                override fun onFinish() {
                    resetTimer()
                }
            }
            timer.start()
        }
    }

    /**
     * Cancels the alarm, notification and resets the timer
     */
    private fun cancelNotification() {
        resetTimer()
        alarmManager.cancel(notifyPendingIntent)
    }

    private fun resetTimer() {
        timer.cancel()
        _elapsedTime.value = 0
        _alarmOn.value = false
    }

    fun loadTime(time: Long) {
        triggerTime = time
    }

    fun setDateSelected(dateInMills: Long) {
        _dateSelection.value = dateInMills
    }
}