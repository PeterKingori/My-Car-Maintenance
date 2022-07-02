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

    private val sharedVehiclesViewModel: VehiclesViewModel by activityViewModels()

    /**
     * Inflates the layout with Data Binding, sets its lifecycle owner to the OverviewFragment
     * to enable Data Binding to observe LiveData, and sets up the RecyclerView with an adapter.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentVehiclesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Initialise the RecyclerView adapter
        val adapter = VehiclesAdapter()
        binding.vehiclesListRecyclerView.adapter = adapter
        adapter.submitList(sharedVehiclesViewModel.vehiclesData)
    }

    /**
     * Frees the binding object when the Fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}