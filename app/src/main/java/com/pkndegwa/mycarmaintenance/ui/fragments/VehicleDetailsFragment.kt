package com.pkndegwa.mycarmaintenance.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.pkndegwa.mycarmaintenance.CarMaintenanceApplication
import com.pkndegwa.mycarmaintenance.R
import com.pkndegwa.mycarmaintenance.data.model.Vehicle
import com.pkndegwa.mycarmaintenance.databinding.FragmentVehicleDetailsBinding
import com.pkndegwa.mycarmaintenance.ui.VehiclesViewModel
import com.pkndegwa.mycarmaintenance.ui.VehiclesViewModelFactory

/**
 * [VehicleDetailsFragment] displays the details of a particular vehicle.
 */
class VehicleDetailsFragment : Fragment() {
    private var _binding: FragmentVehicleDetailsBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val navigationArgs: VehicleDetailsFragmentArgs by navArgs()

    private val viewModel: VehiclesViewModel by activityViewModels {
        VehiclesViewModelFactory((activity?.application as CarMaintenanceApplication).database.vehicleDao())
    }

    private lateinit var vehicle: Vehicle

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment.
        _binding = FragmentVehicleDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vehicleId = navigationArgs.vehicleId
        viewModel.retrieveVehicle(vehicleId).observe(this.viewLifecycleOwner) { selectedVehicle ->
            vehicle = selectedVehicle
            bind(vehicle)
        }
    }

    private fun bind(vehicle: Vehicle) {
        binding.apply {
            vehicleManufacturerAndModel.text = getString(R.string.vehicle_name, vehicle.manufacturer, vehicle.model)
            licenseText.text = vehicle.licensePlate
            fuelText.text = vehicle.fuelType
            mileageText.text = vehicle.mileage.toString()
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