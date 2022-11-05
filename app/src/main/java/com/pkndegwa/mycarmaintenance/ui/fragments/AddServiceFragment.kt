package com.pkndegwa.mycarmaintenance.ui.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.view.children
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.pkndegwa.mycarmaintenance.CarMaintenanceApplication
import com.pkndegwa.mycarmaintenance.databinding.FragmentAddServiceBinding
import com.pkndegwa.mycarmaintenance.viewmodels.ServicesViewModel
import com.pkndegwa.mycarmaintenance.viewmodels.ServicesViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

/**
 * [AddServiceFragment] allows a user to add a new service that has been performed to the vehicle.
 */
class AddServiceFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    private var _binding: FragmentAddServiceBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val navigationArgs: AddServiceFragmentArgs by navArgs()

    private val servicesViewModel: ServicesViewModel by activityViewModels {
        ServicesViewModelFactory((activity?.application as CarMaintenanceApplication).database.serviceDao())
    }

    // Calendar variables
    private val calendar: Calendar = Calendar.getInstance()
    private val simpleDateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    private var formattedDate = ""
    private val year = calendar.get(Calendar.YEAR)
    private val month = calendar.get(Calendar.MONTH)
    private val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
    private var flag = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Retrieve and inflate the layout for this fragment.
        _binding = FragmentAddServiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Sets the dates buttons on first access
        setDate()
        futureDate(6)

        val vehicleId = navigationArgs.vehicleId

        // Click listeners for service dates buttons
        binding.serviceDateButton.setOnClickListener {
            datePicker(requireContext(), true)
            flag = 0
        }

        binding.nextServiceDateButton.setOnClickListener {
            datePicker(requireContext(), false)
            flag = 1
        }

        // Setup a click listener for the Save button.
        binding.saveServiceButton.setOnClickListener {
            addNewService(vehicleId)
        }
    }

    /**
     * Function to set date when none is selected
     */
    private fun setDate() {
        calendar.set(year, month, dayOfMonth)
        val formatDate = simpleDateFormat.format(calendar.time)
        binding.serviceDateButton.text = formatDate
    }

    /**
     * Function to set future date when none is selected
     */
    private fun futureDate(months: Int) {
        calendar.add(Calendar.MONTH, months)
        val futureDate = simpleDateFormat.format(calendar.time)
        binding.nextServiceDateButton.text = futureDate
    }

    /**
     * Function to show date picker
     */
    private fun datePicker(context: Context, notFutureDate: Boolean) {
        val datePickerDialog = DatePickerDialog(
            context,
            this,
            year, month, dayOfMonth
        )
        if (notFutureDate) {
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        }
        datePickerDialog.show()
    }

    /**
     * Function to set date to the button texts
     */
    override fun onDateSet(view: DatePicker?, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int) {
        calendar.set(selectedYear, selectedMonth, selectedDayOfMonth)
        formattedDate = simpleDateFormat.format(calendar.time)
        if (flag == 0) {
            binding.serviceDateButton.text = formattedDate
        } else if (flag == 1) {
            binding.nextServiceDateButton.text = formattedDate
        }
    }

    /**
     * Validates user input before adding the new service in the database using the ViewModel.
     */
    private fun addNewService(vehicleId: Int) {
        if (isServiceSelected(binding.servicesChipGroup) &&
            isEntryValid(binding.currentMileage) &&
            isEntryValid(binding.nextServiceMileage) &&
            isEntryValid(binding.totalCost)
        ) {
            val result = servicesViewModel.addNewService(
                servicesList = getSelectedChipsValues(binding.servicesChipGroup),
                currentMileage = binding.currentMileageEditText.text.toString(),
                nextServiceMileage = binding.nextServiceMileageEditText.text.toString(),
                totalCost = binding.totalCostEditText.text.toString(),
                serviceDate = binding.serviceDateButton.text.toString(),
                nextServiceDate = binding.nextServiceDateButton.text.toString(),
                notes = binding.notesEditText.text.toString(),
                vehicleId = vehicleId
            )
            if (result) {
                Toast.makeText(this.context, "Entry saved successfully", Toast.LENGTH_SHORT).show()
                val action = AddServiceFragmentDirections.actionAddServiceFragmentToVehicleDetailsFragment(vehicleId)
                findNavController().navigate(action)
            }
        }
    }

    /**
     * Get values of selected chips
     */
    private fun getSelectedChipsValues(chipGroup: ChipGroup): String {
        return chipGroup.children
            .toList()
            .filter { (it as Chip).isChecked }
            .joinToString(", ") { (it as Chip).text }
    }

    /**
     * Checks if the input fields have been filled.
     */
    private fun isServiceSelected(chipGroup: ChipGroup): Boolean {
        return if (!servicesViewModel.isServiceSelected(chipGroup)) {
            displayErrorDialog()
            false
        } else true
    }

    /**
     * Shows dialog when no service is selected
     */
    private fun displayErrorDialog() {
        val dialogBuilder: MaterialAlertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
            .setMessage("Please select a service")
            .setCancelable(false)
            .setPositiveButton("Ok") { dialog, _ ->
                dialog.dismiss()
            }
        dialogBuilder.create().show()
    }

    /**
     * Checks if the text input fields have been filled.
     */
    private fun isEntryValid(view: TextInputLayout): Boolean {
        return if (!servicesViewModel.isEntryValid(view.editText?.text.toString())) {
            setError(view)
            removeError(view)
            false
        } else {
            true
        }
    }

    /**
     * Sets the text field error status.
     */
    private fun setError(view: TextInputLayout) {
        view.isErrorEnabled = true
        view.error = "Fill in this field."
    }

    /**
     * Removes the text field error stats.
     */
    private fun removeError(view: TextInputLayout) {
        view.editText?.doOnTextChanged { _, _, _, _ ->
            view.isErrorEnabled = false
            view.error = null
        }
    }

    /**
     * Frees the binding object when the Fragment is destroyed.
     */
    override fun onDestroy() {
        super.onDestroy()
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }
}