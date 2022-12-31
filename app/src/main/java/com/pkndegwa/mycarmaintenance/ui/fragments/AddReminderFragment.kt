package com.pkndegwa.mycarmaintenance.ui.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.pkndegwa.mycarmaintenance.CarMaintenanceApplication
import com.pkndegwa.mycarmaintenance.databinding.FragmentAddReminderBinding
import com.pkndegwa.mycarmaintenance.utils.isEntryValid
import com.pkndegwa.mycarmaintenance.viewmodels.RemindersViewModel
import com.pkndegwa.mycarmaintenance.viewmodels.RemindersViewModelFactory
import java.text.SimpleDateFormat
import java.util.*


/**
 * Use the [AddReminderFragment] to add a reminder.
 */
class AddReminderFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    private var _binding: FragmentAddReminderBinding? = null
    private val binding get() = _binding!!

    private val remindersViewModel: RemindersViewModel by activityViewModels {
        RemindersViewModelFactory((activity?.application as CarMaintenanceApplication).database.reminderDao())
    }

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set the date on first access
        setDate()

        binding.reminderDateButton.setOnClickListener {
            datePicker(requireContext())
        }

        binding.saveReminderButton.setOnClickListener {
            addNewReminder()
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

    private fun addNewReminder() {
        if (isEntryValid(binding.reminderText)) {
            val result = remindersViewModel.addNewReminder(
                reminderText = binding.reminderTextEditText.text.toString(),
                reminderDate = binding.reminderDateButton.text.toString(),
                additionalText = binding.reminderAdditionalTextEditText.text.toString()
            )
            if (result) {
                Toast.makeText(this.context, "Reminder saved successfully", Toast.LENGTH_SHORT).show()
                this.findNavController().navigate(
                    AddReminderFragmentDirections.actionAddReminderFragmentToRemindersFragment()
                )
            }
        }
    }

    /**
     * Frees the binding object when the Fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}