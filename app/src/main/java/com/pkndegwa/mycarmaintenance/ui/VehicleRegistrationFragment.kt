package com.pkndegwa.mycarmaintenance.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.pkndegwa.mycarmaintenance.R
import com.pkndegwa.mycarmaintenance.databinding.FragmentVehicleRegistrationBinding
import com.pkndegwa.mycarmaintenance.model.VehiclesViewModel

/**
 * [VehicleRegistrationFragment] allows a user to add details of a vehicle to be registered.
 */
class VehicleRegistrationFragment : Fragment() {

    companion object {
        const val MANUFACTURER_NAME = "manufacturerName"
    }

    private var _binding: FragmentVehicleRegistrationBinding? = null
    private var manufacturerNameId: String = ""

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val sharedVehiclesViewModel: VehiclesViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            manufacturerNameId = it.getString(MANUFACTURER_NAME).toString()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Retrieve and inflate the layout for this fragment.
        _binding = FragmentVehicleRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            viewModel = sharedVehiclesViewModel
        }

        binding.vehicleTypeEditText.setOnClickListener {
            showMenu(it, R.menu.popup_menu)
        }

        binding.vehicleManufacturerEditText.setOnClickListener {
            val action =
                VehicleRegistrationFragmentDirections.actionVehicleRegistrationFragmentToVehicleManufacturersFragment()
            view.findNavController().navigate(action)
        }

        sharedVehiclesViewModel.setVehicleManufacturer(manufacturerNameId)

        binding.registerVehicleButton.setOnClickListener {
            setVehicleDetails()
        }
    }

    /**
     * Shows a menu to select a vehicle type.
     */
    private fun showMenu(view: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(context!!, view)
        popup.menuInflater.inflate(menuRes, popup.menu)
        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            binding.vehicleTypeEditText.setText(menuItem.title)
            sharedVehiclesViewModel.setVehicleType(menuItem.title.toString())
            return@setOnMenuItemClickListener true
        }
        popup.show()
    }

    /**
     * Sets the entered vehicle details in the [VehiclesViewModel].
     */
    private fun setVehicleDetails() {
//        val vehicleType = binding.vehicleTypeEditText.text.toString()
//        val manufacturer = binding.vehicleManufacturerEditText.text.toString()
//        val model = binding.vehicleModelEditText.text.toString()
//        val licensePlate = binding.vehicleLicensePlateEditText.text.toString()
//        val modelYear = binding.vehicleModelYearEditText.text.toString().toInt()
//        val fuelType = binding.vehicleFuelTypeEditText.text.toString()
//        val fuelCapacity = binding.vehicleFuelCapacityEditText.text.toString().toDouble()
//        val mileage = binding.vehicleMileageEditText.text.toString().toInt()
//        val chassisNumber = binding.vehicleChassisNumberEditText.text.toString()
//
//        sharedVehiclesViewModel.setVehicleDetails(
//            type = vehicleType,
//            vehicleManufacturer = manufacturer,
//            vehicleModel = model,
//            vehicleLicensePlate = licensePlate,
//            vehicleModelYear = modelYear,
//            vehicleFuelType = fuelType,
//            vehicleFuelCapacity = fuelCapacity,
//            vehicleMileage = mileage,
//            vehicleChassisNumber = chassisNumber
//        )

        val action = VehicleRegistrationFragmentDirections.actionVehicleRegistrationFragmentToVehiclesFragment()
        findNavController().navigate(action)
    }

    /**
     * Frees the binding object when the Fragment is destroyed.
     */
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}