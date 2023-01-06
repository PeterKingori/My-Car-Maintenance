package com.pkndegwa.mycarmaintenance.ui.fragments

import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pkndegwa.mycarmaintenance.CarMaintenanceApplication
import com.pkndegwa.mycarmaintenance.R
import com.pkndegwa.mycarmaintenance.adapter.ServiceListAdapter
import com.pkndegwa.mycarmaintenance.databinding.FragmentVehicleDetailsBinding
import com.pkndegwa.mycarmaintenance.models.Vehicle
import com.pkndegwa.mycarmaintenance.viewmodels.ServicesViewModel
import com.pkndegwa.mycarmaintenance.viewmodels.VehiclesViewModel
import com.pkndegwa.mycarmaintenance.viewmodels.createFactory

/**
 * [VehicleDetailsFragment] displays the details of a particular vehicle.
 */
class VehicleDetailsFragment : Fragment() {
    private var _binding: FragmentVehicleDetailsBinding? = null

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

    private lateinit var servicesViewModel: ServicesViewModel
    private fun initServicesViewModel() {
        val factory =
            ServicesViewModel((activity?.application as CarMaintenanceApplication).database.serviceDao()).createFactory()
        servicesViewModel = ViewModelProvider(this, factory)[ServicesViewModel::class.java]
    }

    private lateinit var servicesListAdapter: ServiceListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment.
        _binding = FragmentVehicleDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVehiclesViewModel()
        initServicesViewModel()

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()
                menuInflater.inflate(R.menu.vehicle_details_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.delete_option -> {
                        showConfirmationDialogForVehicle()
                        true
                    }
                    R.id.edit_option -> {
                        editVehicle()
                        true
                    }
                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        val vehicleId = navigationArgs.vehicleId
        vehiclesViewModel.retrieveVehicle(vehicleId)?.observe(this.viewLifecycleOwner) { selectedVehicle ->
            vehicle = selectedVehicle
            bind(vehicle)
        }

        setUpServicesRecyclerView(vehicleId)

        binding.addNewService.setOnClickListener {
            this.findNavController()
                .navigate(
                    VehicleDetailsFragmentDirections.actionVehicleDetailsFragmentToAddServiceFragment
                        (vehicleId, getString(R.string.add_new_service))
                )
        }
    }

    /**
     * Binds the vehicle data retrieved using the ViewModel to the TextViews in the Vehicle Details layout.
     */
    private fun bind(vehicle: Vehicle) {
        binding.apply {
            vehicleManufacturerAndModel.text = getString(R.string.vehicle_name, vehicle.manufacturer, vehicle.model)
            licenseText.text = vehicle.licensePlate
            fuelText.text = vehicle.fuelType
            mileageText.text = getString(R.string.formatted_vehicle_mileage, vehicle.mileage)
            modelYearText.text = vehicle.modelYear.toString()
        }
        val vehicleImageUri = Uri.parse(vehicle.vehicleImageUri)
        Glide.with(requireContext())
            .load(vehicleImageUri)
            .centerInside()
            .error(AppCompatResources.getDrawable(requireContext(), R.drawable.generic_car))
            .fallback(AppCompatResources.getDrawable(requireContext(), R.drawable.generic_car))
            .into(binding.vehicleImage)
    }

    /**
     * Allows the user to edit vehicle details.
     */
    private fun editVehicle() {
        val action = VehicleDetailsFragmentDirections.actionVehicleDetailsFragmentToVehicleRegistrationFragment(
            getString(R.string.edit_fragment_title),
            vehicle.id
        )
        this.findNavController().navigate(action)
    }

    /**
     * Displays an alert dialog to get the user's confirmation before deleting the vehicle.
     */
    private fun showConfirmationDialogForVehicle() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(getString(R.string.delete_vehicle_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.cancel), null)
            .setPositiveButton(getString(R.string.delete)) { _, _ ->
                deleteVehicle()
            }
            .show()
    }

    /**
     * Deletes the current vehicle and navigates to the Home Fragment.
     */
    private fun deleteVehicle() {
        vehiclesViewModel.deleteVehicle(vehicle)
        val action = VehicleDetailsFragmentDirections.actionVehicleDetailsFragmentToHomeFragment()
        this.findNavController().navigate(action)
    }

    /**
     * RecyclerView that displays the list of services done for a particular vehicle
     */
    private fun setUpServicesRecyclerView(vehicleId: Int) {
        servicesListAdapter = ServiceListAdapter {
            // Displays an alert dialog to get the user's confirmation before deleting the service.
            MaterialAlertDialogBuilder(requireContext())
                .setMessage(getString(R.string.delete_service_question))
                .setCancelable(false)
                .setNegativeButton(getString(R.string.cancel), null)
                .setPositiveButton(getString(R.string.delete)) { _, _ ->
                    servicesViewModel.deleteService(it)
                }
                .show()
        }

        binding.servicesListRecyclerView.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = servicesListAdapter
        }

        servicesViewModel.getAllServicesForVehicle(vehicleId).observe(viewLifecycleOwner) { services ->
            servicesListAdapter.submitList(services)
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