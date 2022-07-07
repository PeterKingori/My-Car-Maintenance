package com.pkndegwa.mycarmaintenance.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pkndegwa.mycarmaintenance.databinding.FragmentVehicleDetailsBinding

/**
 * [VehicleDetailsFragment] displays the details of a particular vehicle.
 */
class VehicleDetailsFragment : Fragment() {
    private var _binding: FragmentVehicleDetailsBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment.
        _binding = FragmentVehicleDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Frees the binding object when the Fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}