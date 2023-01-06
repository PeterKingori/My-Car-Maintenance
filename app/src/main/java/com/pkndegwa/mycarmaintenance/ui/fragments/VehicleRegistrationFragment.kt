package com.pkndegwa.mycarmaintenance.ui.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.MenuRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.pkndegwa.mycarmaintenance.CarMaintenanceApplication
import com.pkndegwa.mycarmaintenance.R
import com.pkndegwa.mycarmaintenance.databinding.FragmentVehicleRegistrationBinding
import com.pkndegwa.mycarmaintenance.models.Vehicle
import com.pkndegwa.mycarmaintenance.utils.ImageCapture
import com.pkndegwa.mycarmaintenance.utils.isEntryValid
import com.pkndegwa.mycarmaintenance.viewmodels.VehiclesViewModel
import com.pkndegwa.mycarmaintenance.viewmodels.createFactory

/**
 * [VehicleRegistrationFragment] allows a user to add details of a vehicle to be registered.
 */
class VehicleRegistrationFragment : Fragment() {
    private var _binding: FragmentVehicleRegistrationBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val navigationArgs: VehicleDetailsFragmentArgs by navArgs()

    private lateinit var vehiclesViewModel: VehiclesViewModel
    private fun initVehiclesViewModel() {
        val factory =
            VehiclesViewModel((activity?.application as CarMaintenanceApplication).database.vehicleDao()).createFactory()
        vehiclesViewModel = ViewModelProvider(this, factory).get(VehiclesViewModel::class.java)
    }

    private lateinit var vehicle: Vehicle
    private var selectedImageUri: Uri? = null

    private val openGalleryLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                selectedImageUri = result.data?.data
                val vehicleImageView = binding.addVehicleImage
                vehicleImageView.setImageURI(selectedImageUri)
                vehicleImageView.scaleType = ImageView.ScaleType.CENTER_CROP
                binding.addPhotoTextView.visibility = View.GONE
            }
        }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            val selectImageIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            openGalleryLauncher.launch(selectImageIntent)
        } else {
            Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Retrieve and inflate the layout for this fragment.
        _binding = FragmentVehicleRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVehiclesViewModel()

        val vehicleId = navigationArgs.vehicleId
        if (vehicleId > 0) {
            vehiclesViewModel.retrieveVehicle(vehicleId)?.observe(this.viewLifecycleOwner) { selectedVehicle ->
                vehicle = selectedVehicle
                bind(vehicle)
            }
        } else {
            // Setup a click listener for the Save button when creating a new Vehicle record.
            binding.saveVehicleButton.setOnClickListener {
                addNewVehicle()
            }
        }

        binding.apply {
            // Setup a click listener for the Vehicle type EditText to show a menu.
            vehicleTypeEditText.setOnClickListener {
                showMenu(binding.vehicleTypeEditText, R.menu.popup_menu_vehicle_type)
            }

            // Setup a click listener for the Fuel type EditText to show a menu.
            vehicleFuelTypeEditText.setOnClickListener {
                showMenu(binding.vehicleFuelTypeEditText, R.menu.popup_menu_fuel_type)
            }

            // Setup a click listener for the Cancel button.
            cancelRegisterButton.setOnClickListener {
                cancelRegistration(vehicleId)
            }

            // Setup a click listener for the Add photo button.
            addVehicleImage.setOnClickListener {
                val imageCapture = ImageCapture(
                    requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE,
                    requireActivity(), requestPermissionLauncher
                )
                imageCapture.askForPermission()
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
     * Validates user input before adding the new vehicle in the database using the ViewModel.
     */
    private fun addNewVehicle() {
        if (isEntryValid(binding.vehicleType) &&
            isEntryValid(binding.vehicleManufacturer) &&
            isEntryValid(binding.vehicleModel) &&
            isEntryValid(binding.vehicleModelYear) &&
            isEntryValid(binding.vehicleLicensePlate) &&
            isEntryValid(binding.vehicleFuelType) &&
            isEntryValid(binding.vehicleMileage)
        ) {
            vehiclesViewModel.addNewVehicle(
                vehicleImageUri = selectedImageUri.toString(),
                vehicleType = binding.vehicleTypeEditText.text.toString(),
                vehicleManufacturer = binding.vehicleManufacturerEditText.text.toString(),
                vehicleModel = binding.vehicleModelEditText.text.toString(),
                vehicleModelYear = binding.vehicleModelYearEditText.text.toString(),
                vehicleLicensePlate = binding.vehicleLicensePlateEditText.text.toString(),
                vehicleFuelType = binding.vehicleFuelTypeEditText.text.toString(),
                vehicleMileage = binding.vehicleMileageEditText.text.toString()
            )
            Toast.makeText(this.context, "Vehicle saved successfully", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_vehicleRegistrationFragment_to_homeFragment)
        }
    }

    /**
     * Cancels the registration.
     */
    private fun cancelRegistration(vehicleId: Int) {
        clearText()
        if (vehicleId == -1) {
            findNavController().navigate(R.id.action_vehicleRegistrationFragment_to_homeFragment)
        } else if (vehicleId > 0) {
            findNavController().navigate(
                VehicleRegistrationFragmentDirections
                    .actionVehicleRegistrationFragmentToVehicleDetailsFragment(vehicleId)
            )
        }
    }

    /**
     * Clear the text fields.
     */
    private fun clearText() {
        binding.apply {
            vehicleTypeEditText.text = null
            vehicleManufacturerEditText.text = null
            vehicleModelEditText.text = null
            vehicleModelYearEditText.text = null
            vehicleLicensePlateEditText.text = null
            vehicleFuelTypeEditText.text = null
            vehicleMileageEditText.text = null
        }
    }

    /**
     * Binds the vehicle data to the TextViews when the Edit menu option has been selected in the
     * VehicleDetailsFragment.
     */
    private fun bind(vehicle: Vehicle) {
        binding.apply {
            vehicleTypeEditText.setText(vehicle.type, TextView.BufferType.SPANNABLE)
            vehicleManufacturerEditText.setText(vehicle.manufacturer, TextView.BufferType.SPANNABLE)
            vehicleModelEditText.setText(vehicle.model, TextView.BufferType.SPANNABLE)
            vehicleModelYearEditText.setText(vehicle.modelYear.toString(), TextView.BufferType.SPANNABLE)
            vehicleLicensePlateEditText.setText(vehicle.licensePlate, TextView.BufferType.SPANNABLE)
            vehicleFuelTypeEditText.setText(vehicle.fuelType, TextView.BufferType.SPANNABLE)
            vehicleMileageEditText.setText(vehicle.mileage.toString(), TextView.BufferType.SPANNABLE)

            saveVehicleButton.setOnClickListener { updateVehicle() }
        }
        val vehicleImageUri = Uri.parse(vehicle.vehicleImageUri)
        Glide.with(requireContext())
            .load(vehicleImageUri)
            .centerCrop()
            .placeholder(AppCompatResources.getDrawable(requireContext(), R.drawable.generic_car))
            .error(AppCompatResources.getDrawable(requireContext(), R.drawable.generic_car))
            .fallback(AppCompatResources.getDrawable(requireContext(), R.drawable.generic_car))
            .into(binding.addVehicleImage)
        binding.addPhotoTextView.visibility = View.GONE
    }

    /**
     * Validates user input before updating the vehicle details in the database using the ViewModel.
     */
    private fun updateVehicle() {
        if (isEntryValid(binding.vehicleType) &&
            isEntryValid(binding.vehicleManufacturer) &&
            isEntryValid(binding.vehicleModel) &&
            isEntryValid(binding.vehicleModelYear) &&
            isEntryValid(binding.vehicleLicensePlate) &&
            isEntryValid(binding.vehicleFuelType) &&
            isEntryValid(binding.vehicleMileage)
        ) {
            val result = vehiclesViewModel.updateVehicle(
                vehicleId = this.navigationArgs.vehicleId,
                vehicleImageUri = selectedImageUri.toString(),
                vehicleType = this.binding.vehicleTypeEditText.text.toString(),
                vehicleManufacturer = this.binding.vehicleManufacturerEditText.text.toString(),
                vehicleModel = this.binding.vehicleModelEditText.text.toString(),
                vehicleModelYear = this.binding.vehicleModelYearEditText.text.toString(),
                vehicleLicensePlate = this.binding.vehicleLicensePlateEditText.text.toString(),
                vehicleFuelType = this.binding.vehicleFuelTypeEditText.text.toString(),
                vehicleMileage = this.binding.vehicleMileageEditText.text.toString()
            )
            if (result) {
                Toast.makeText(this.context, "Vehicle updated successfully", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_vehicleRegistrationFragment_to_homeFragment)
            }
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