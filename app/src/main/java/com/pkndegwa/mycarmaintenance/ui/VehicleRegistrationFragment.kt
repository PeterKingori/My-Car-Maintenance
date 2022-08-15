package com.pkndegwa.mycarmaintenance.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.pkndegwa.mycarmaintenance.R
import com.pkndegwa.mycarmaintenance.databinding.FragmentVehicleRegistrationBinding
import com.pkndegwa.mycarmaintenance.model.Vehicle

/**
 * [VehicleRegistrationFragment] allows a user to add details of a vehicle to be registered.
 */
class VehicleRegistrationFragment : Fragment() {

    private var _binding: FragmentVehicleRegistrationBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Retrieve and inflate the layout for this fragment.
        _binding = FragmentVehicleRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            vehicleRegistrationFragment = this@VehicleRegistrationFragment

            // Setup a click listener for the Vehicle type EditText to show a menu.
            vehicleTypeEditText.setOnClickListener {
                showMenu(binding.vehicleTypeEditText, R.menu.popup_menu_vehicle_type)
            }

            // Setup a click listener for the Fuel type EditText to show a menu.
            vehicleFuelTypeEditText.setOnClickListener {
                showMenu(binding.vehicleFuelTypeEditText, R.menu.popup_menu_fuel_type)
            }
        }

        // Setup a click listener for the Register buttons.
        binding.registerVehicleButton.setOnClickListener {
            if (checkInputFields(binding.vehicleType) &&
                checkInputFields(binding.vehicleManufacturer) &&
                checkInputFields(binding.vehicleModel) &&
                checkInputFields(binding.vehicleLicensePlate) &&
                checkInputFields(binding.vehicleFuelType) &&
                checkInputFields(binding.vehicleMileage)
            ) {
                registerVehicle()
            }
        }
    }

    /**
     * Shows a menu to select a certain property of a vehicle.
     */
    private fun showMenu(view: EditText, @MenuRes menuRes: Int) {
        val popup = PopupMenu(requireContext(), view)
        popup.menuInflater.inflate(menuRes, popup.menu)
        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            view.setText(menuItem.title)
            return@setOnMenuItemClickListener true
        }
        popup.show()
    }

    /**
     * Checks if the fields have been filled and proceeds to the fragment showing the registered vehicles.
     */
    private fun checkInputFields(view: TextInputLayout): Boolean {
        return if (view.editText?.text.toString() == "") {
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
     * Creates a Vehicle instance, saves the data and navigates to the VehiclesFragment.
     */
    private fun registerVehicle() {
        val type = binding.vehicleTypeEditText.text.toString()
        val manufacturer = binding.vehicleManufacturerEditText.text.toString()
        val model = binding.vehicleModelEditText.text.toString()
        val licensePlate = binding.vehicleLicensePlateEditText.text.toString()
        val fuel = binding.vehicleFuelTypeEditText.text.toString()
        val mileage = binding.vehicleMileageEditText.text.toString().toInt()

        val vehicle = Vehicle(type, manufacturer, model, licensePlate, fuel, mileage)

        val action = VehicleRegistrationFragmentDirections.actionVehicleRegistrationFragmentToVehiclesFragment()
        findNavController().navigate(action)
    }

    /**
     * Cancels the registration.
     */
    fun cancelRegistration() {
        clearText()
        findNavController().navigate(R.id.action_vehicleRegistrationFragment_to_welcomeFragment)
    }

    /**
     * Clear the text fields.
     */
    private fun clearText() {
        binding.apply {
            vehicleTypeEditText.text = null
            vehicleManufacturerEditText.text = null
            vehicleModelEditText.text = null
            vehicleLicensePlateEditText.text = null
            vehicleFuelTypeEditText.text = null
            vehicleMileageEditText.text = null
        }
    }

    /**
     * Frees the binding object when the Fragment is destroyed.
     */
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}