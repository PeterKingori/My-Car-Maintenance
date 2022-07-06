package com.pkndegwa.mycarmaintenance.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.pkndegwa.mycarmaintenance.R
import com.pkndegwa.mycarmaintenance.databinding.FragmentWelcomeBinding

/**
 * [WelcomeFragment] allows a user to click a button to register a vehicle.
 */
class WelcomeFragment : Fragment() {
    private var _binding: FragmentWelcomeBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Retrieve and inflate the layout for this fragment
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Launch the VehicleRegistrationFragment on addVehicleButton click
        binding.addVehicleButton.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_vehicleRegistrationFragment)
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