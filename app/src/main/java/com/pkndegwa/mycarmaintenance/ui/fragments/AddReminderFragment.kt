package com.pkndegwa.mycarmaintenance.ui.fragments

import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pkndegwa.mycarmaintenance.CarMaintenanceApplication
import com.pkndegwa.mycarmaintenance.R
import com.pkndegwa.mycarmaintenance.databinding.FragmentAddReminderBinding
import com.pkndegwa.mycarmaintenance.models.Reminder
import com.pkndegwa.mycarmaintenance.utils.isEntryValid
import com.pkndegwa.mycarmaintenance.viewmodels.RemindersViewModel
import com.pkndegwa.mycarmaintenance.viewmodels.createFactory
import java.text.SimpleDateFormat
import java.util.*


/**
 * Use the [AddReminderFragment] to add a reminder.
 */
class AddReminderFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    private var _binding: FragmentAddReminderBinding? = null
    private val binding get() = _binding!!

    private lateinit var remindersViewModel: RemindersViewModel
    private fun initRemindersViewModel() {
        val factory = RemindersViewModel(
            requireActivity().application,
            (activity?.application as CarMaintenanceApplication).database
                .reminderDao()
        )
            .createFactory()
        remindersViewModel = ViewModelProvider(this, factory)[RemindersViewModel::class.java]
    }

    private val navigationArgs: AddReminderFragmentArgs by navArgs()
    private lateinit var reminder: Reminder

    // Calendar variables
    private val calendar: Calendar = Calendar.getInstance()
    private val year = calendar.get(Calendar.YEAR)
    private val month = calendar.get(Calendar.MONTH)
    private val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
    private val simpleDateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    private var formattedDate = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddReminderBinding.inflate(inflater, container, false)

        // Call create channel
        createChannel(
            getString(R.string.reminder_notification_channel_id),
            getString(R.string.reminder_notification_channel_name)
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRemindersViewModel()

        val reminderId = navigationArgs.id
        if (reminderId > 0) {
            remindersViewModel.retrieveReminder(reminderId).observe(this.viewLifecycleOwner) { selectedReminder ->
                reminder = selectedReminder
                bindDetails(reminder)
            }

            val menuHost: MenuHost = requireActivity()
            menuHost.addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menu.clear()
                    menuInflater.inflate(R.menu.delete_menu_item, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.delete_reminder -> {
                            showConfirmationDialogForReminder()
                            true
                        }
                        else -> false
                    }
                }

            }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        } else {
            binding.saveReminderButton.setOnClickListener {
                addNewReminder()
            }
        }

        // Set the date on first access
        setDate()

        binding.reminderDateButton.setOnClickListener {
            datePicker(requireContext())
        }
    }

    /**
     * Function to set date when none is selected
     */
    private fun setDate() {
        calendar.set(year, month, dayOfMonth)
        val formatDate = simpleDateFormat.format(calendar.time)
        binding.reminderDateButton.text = formatDate
    }

    /**
     * Function to show date picker
     */
    private fun datePicker(context: Context) {
        val datePickerDialog = DatePickerDialog(context, this, year, month, dayOfMonth)
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    /**
     * Function to set date to the button texts
     */
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        calendar.set(year, month, dayOfMonth)
        formattedDate = simpleDateFormat.format(calendar.time)
        binding.reminderDateButton.text = formattedDate
    }

    /**
     * Validates user input before adding a new reminder in the database using the ViewModel.
     */
    private fun addNewReminder() {
        if (isEntryValid(binding.reminderText)) {
            val result = remindersViewModel.addNewReminder(
                reminderText = binding.reminderTextEditText.text.toString(),
                reminderDate = binding.reminderDateButton.text.toString(),
                additionalText = binding.reminderAdditionalTextEditText.text.toString()
            )
            if (result) {
                remindersViewModel.loadTime(calendar.timeInMillis)
                remindersViewModel.setDateSelected(calendar.timeInMillis)
                remindersViewModel.setAlarm(true)
                Toast.makeText(this.context, "Reminder saved successfully", Toast.LENGTH_SHORT).show()
                this.findNavController().navigate(
                    AddReminderFragmentDirections.actionAddReminderFragmentToRemindersFragment()
                )
            }
        }
    }

    /**
     * Binds the reminder data to the TextViews when a reminder is selected in the RemindersFragment.
     */
    private fun bindDetails(reminder: Reminder) {
        binding.apply {
            reminderTextEditText.setText(reminder.reminderText, TextView.BufferType.SPANNABLE)
            reminderDateButton.text = reminder.reminderDate
            reminderAdditionalTextEditText.setText(reminder.additionalText, TextView.BufferType.SPANNABLE)

            saveReminderButton.setOnClickListener { updateReminder() }
        }
    }

    /**
     * Validates user input before updating the reminder details in the database using the ViewModel.
     */
    private fun updateReminder() {
        if (isEntryValid(binding.reminderText)) {
            val result = remindersViewModel.updateReminder(
                reminderId = this.navigationArgs.id,
                reminderText = this.binding.reminderTextEditText.text.toString(),
                reminderDate = this.binding.reminderDateButton.text.toString(),
                additionalText = this.binding.reminderAdditionalTextEditText.text.toString()
            )
            if (result) {
                remindersViewModel.loadTime(calendar.timeInMillis)
                remindersViewModel.setDateSelected(calendar.timeInMillis)
                remindersViewModel.setAlarm(true)
                Toast.makeText(this.context, "Reminder updated successfully", Toast.LENGTH_SHORT).show()
                findNavController().navigate(AddReminderFragmentDirections.actionAddReminderFragmentToRemindersFragment())
            }
        }
    }

    /**
     * Displays an alert dialog to get the user's confirmation before deleting the reminder.
     */
    private fun showConfirmationDialogForReminder() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage("Are you sure you want to delete the reminder?")
            .setCancelable(false)
            .setNegativeButton(getString(R.string.cancel), null)
            .setPositiveButton(getString(R.string.delete)) { _, _ ->
                deleteReminder()
            }
            .show()
    }

    private fun deleteReminder() {
        remindersViewModel.deleteReminder(reminder)
        this.findNavController().navigate(AddReminderFragmentDirections.actionAddReminderFragmentToRemindersFragment())
    }

    private fun createChannel(channelId: String, channelName: String) {
        val notificationChannel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply { setShowBadge(false) }

        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.YELLOW
        notificationChannel.enableVibration(true)

        val notificationManager = requireActivity().getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(notificationChannel)
    }

    /**
     * Frees the binding object when the Fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}