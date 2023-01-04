package com.pkndegwa.mycarmaintenance.ui.fragments

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pkndegwa.mycarmaintenance.CarMaintenanceApplication
import com.pkndegwa.mycarmaintenance.R
import com.pkndegwa.mycarmaintenance.databinding.FragmentAddServiceBinding
import com.pkndegwa.mycarmaintenance.models.Service
import com.pkndegwa.mycarmaintenance.utils.ImageCapture
import com.pkndegwa.mycarmaintenance.utils.isEntryValid
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

    private lateinit var service: Service

    // Calendar variables
    private val calendar: Calendar = Calendar.getInstance()
    private val simpleDateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    private var formattedDate = ""
    private val year = calendar.get(Calendar.YEAR)
    private val month = calendar.get(Calendar.MONTH)
    private val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
    private var flag = 0

    private var selectedReceiptImageUri: Uri? = null
//    private val receiptImagesList = mutableListOf<String>()

    private val openGalleryLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                selectedReceiptImageUri = result.data?.data
                binding.receiptImageCard.visibility = View.VISIBLE
                val receiptImage = binding.receiptImage
                receiptImage.setImageURI(selectedReceiptImageUri)
                receiptImage.scaleType = ImageView.ScaleType.CENTER_CROP
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
        _binding = FragmentAddServiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Sets the dates buttons on first access
        setDate()
        futureDate()

        val vehicleId = navigationArgs.vehicleId
        val serviceId = navigationArgs.serviceId
        // Check if it's an update to an existing service or a new one being created
        if (serviceId > 0) {
            servicesViewModel.retrieveService(serviceId)?.observe(this.viewLifecycleOwner) { selectedService ->
                service = selectedService
                bind(service)
            }
        } else {
            // Setup click listener for the Save button when creating a new Service record.
            binding.saveServiceButton.setOnClickListener {
                addNewService(vehicleId)
            }
        }

        binding.apply {
            // Click listeners for service dates buttons
            serviceDateButton.setOnClickListener {
                datePicker(requireContext(), true)
                flag = 0
            }
            nextServiceDateButton.setOnClickListener {
                datePicker(requireContext(), false)
                flag = 1
            }

            // Click listener to add a new service that isn't present in the chip group
            addNewServiceChip.setOnClickListener {
                val position = binding.servicesChipGroup.size - 1
                addNewServiceTypeDialog(position)
            }

            // Click listener to add a photo of a receipt
            addReceiptPhotoButton.setOnClickListener {
                val imageCapture = ImageCapture(
                    requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE,
                    requireActivity(), requestPermissionLauncher
                )
                imageCapture.askForPermission()
            }
            removeReceiptImageIcon.setOnClickListener {
                selectedReceiptImageUri = null
                binding.receiptImageCard.visibility = View.GONE
            }
        }
    }

    // RecyclerView for showing multiple receipt images
//    private fun setupReceiptsRecyclerView() {
//        val receiptImagesAdapter = FileImageListAdapter()
//        val horizontalLayoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
//        binding.receiptImagesRecyclerView.apply {
//            layoutManager = horizontalLayoutManager
//            adapter = receiptImagesAdapter
//        }
//        receiptImagesAdapter.submitList(receiptImagesList)
//    }

    /**
     * Displays a dialog to allow the user enter the name of a service type
     */
    private fun addNewServiceTypeDialog(position: Int) {
        val inflater: LayoutInflater = LayoutInflater.from(requireContext())
        val dialogTextInput = inflater.inflate(R.layout.dialog_text_input, ConstraintLayout(requireContext()))
        val inputEditText = dialogTextInput.findViewById<EditText>(R.id.add_new_service_dialog_edit_text)
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(getString(R.string.enter_service_dialog))
            .setView(dialogTextInput)
            .setCancelable(false)
            .setNegativeButton(getString(R.string.cancel), null)
            .setPositiveButton("Ok") { _, _ ->
                binding.servicesChipGroup.addView(
                    createChip(requireContext(), inputEditText.text.toString()),
                    position
                )
            }
            .show()
    }

    /**
     * Function to create new chips with attributes
     */
    private fun createChip(context: Context, chipName: String): Chip {
        return Chip(context).apply {
            text = chipName
            isCheckable = true
            checkedIconTint = ContextCompat.getColorStateList(context, R.color.chip_color_state_list)
            setCheckedIconResource(R.drawable.ic_baseline_check_24)
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
    private fun futureDate() {
        calendar.add(Calendar.MONTH, 6)
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
                receiptImageUri = selectedReceiptImageUri?.run { toString() } ?: "",
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
     * Set previously saved services as checked when in Edit mode
     */
    private fun setSelectedChipValues(chipGroup: ChipGroup, servicesDone: String) {
        chipGroup.children.forEach {
            if (servicesDone.contains((it as Chip).text)) it.isChecked = true
        }
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
     * Binds the service data to the views.
     */
    private fun bind(service: Service) {
        binding.apply {
            setSelectedChipValues(servicesChipGroup, service.servicesDoneList)
            currentMileageEditText.setText(service.currentMileage.toString(), TextView.BufferType.SPANNABLE)
            nextServiceMileageEditText.setText(service.nextServiceMileage.toString(), TextView.BufferType.SPANNABLE)
            totalCostEditText.setText(service.totalCost.toString(), TextView.BufferType.SPANNABLE)
            serviceDateButton.text = service.serviceDate
            nextServiceDateButton.text = service.nextServiceDate
            notesEditText.setText(service.notes, TextView.BufferType.SPANNABLE)

            saveServiceButton.setOnClickListener { updateService() }
        }

        if (service.receiptImageUriString.isNotEmpty()) {
            val receiptImageUri = Uri.parse(service.receiptImageUriString)
            binding.receiptImageCard.visibility = View.VISIBLE
            val receiptImage = binding.receiptImage
            receiptImage.setImageURI(receiptImageUri)
            receiptImage.scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }

    /**
     * Validates user input before updating the service details in the database using the ViewModel.
     */
    private fun updateService() {
        if (isServiceSelected(binding.servicesChipGroup) &&
            isEntryValid(binding.currentMileage) &&
            isEntryValid(binding.nextServiceMileage) &&
            isEntryValid(binding.totalCost)
        ) {
            val result = servicesViewModel.updateService(
                serviceId = this.navigationArgs.serviceId,
                servicesList = getSelectedChipsValues(this.binding.servicesChipGroup),
                currentMileage = this.binding.currentMileageEditText.text.toString(),
                nextServiceMileage = this.binding.nextServiceMileageEditText.text.toString(),
                totalCost = this.binding.totalCostEditText.text.toString(),
                serviceDate = this.binding.serviceDateButton.text.toString(),
                nextServiceDate = this.binding.nextServiceDateButton.text.toString(),
                notes = this.binding.notesEditText.text.toString(),
                receiptImageUri = selectedReceiptImageUri?.run { toString() } ?: "",
                vehicleId = this.navigationArgs.vehicleId
            )
            if (result) {
                Toast.makeText(this.context, "Entry updated successfully", Toast.LENGTH_SHORT).show()
                val action =
                    AddServiceFragmentDirections.actionAddServiceFragmentToVehicleDetailsFragment(this.navigationArgs.vehicleId)
                findNavController().navigate(action)
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