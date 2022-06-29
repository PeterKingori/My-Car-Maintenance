package com.pkndegwa.mycarmaintenance.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.pkndegwa.mycarmaintenance.databinding.FragmentVehicleRegistrationBinding

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
        binding.vehicleManufacturerEditText.setOnClickListener {
            val action =
                VehicleRegistrationFragmentDirections.actionVehicleRegistrationFragmentToVehicleManufacturersFragment()
            view.findNavController().navigate(action)
        }

        if (manufacturerNameId == "null") {
            binding.vehicleManufacturerEditText.setText("")
        } else {
            binding.vehicleManufacturerEditText.setText(manufacturerNameId)
        }

        binding.registerVehicleButton.setOnClickListener {
            val action = VehicleRegistrationFragmentDirections.actionVehicleRegistrationFragmentToVehiclesFragment()
            view.findNavController().navigate(action)
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