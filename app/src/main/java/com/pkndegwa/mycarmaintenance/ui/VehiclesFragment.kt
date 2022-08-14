package com.pkndegwa.mycarmaintenance.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.pkndegwa.mycarmaintenance.adapter.VehiclesAdapter
import com.pkndegwa.mycarmaintenance.databinding.FragmentVehiclesBinding
import com.pkndegwa.mycarmaintenance.model.VehiclesViewModel

/**
 * [VehiclesFragment] displays a list of registered vehicles.
 */
class VehiclesFragment : Fragment() {
    private var _binding: FragmentVehiclesBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val vehiclesViewModel: VehiclesViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment.
        _binding = FragmentVehiclesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Initialise the RecyclerView adapter
        val recyclerView = binding.vehiclesListRecyclerView
        recyclerView.adapter = VehiclesAdapter(vehiclesViewModel.vehiclesData)
    }

    /**
     * Frees the binding object when the Fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}