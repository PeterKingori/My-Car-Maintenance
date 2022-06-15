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
    private var _binding: FragmentVehicleRegistrationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentVehicleRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.vehicleManufacturerEditText.setOnClickListener {
            val action =
                VehicleRegistrationFragmentDirections.actionVehicleRegistrationFragmentToVehicleManufacturersFragment()
            view.findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}