package com.pkndegwa.mycarmaintenance.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pkndegwa.mycarmaintenance.CarMaintenanceApplication
import com.pkndegwa.mycarmaintenance.R
import com.pkndegwa.mycarmaintenance.adapter.VehicleListAdapter
import com.pkndegwa.mycarmaintenance.databinding.FragmentHomeBinding
import com.pkndegwa.mycarmaintenance.utils.EmptyDataObserver
import com.pkndegwa.mycarmaintenance.viewmodels.VehiclesViewModel
import com.pkndegwa.mycarmaintenance.viewmodels.createFactory

/**
 * [HomeFragment] allows a user to click a button to register a vehicle.
 */
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private lateinit var vehiclesViewModel: VehiclesViewModel
    private fun initVehiclesViewModel() {
        val factory =
            VehiclesViewModel((activity?.application as CarMaintenanceApplication).database.vehicleDao()).createFactory()
        vehiclesViewModel = ViewModelProvider(this, factory)[VehiclesViewModel::class.java]
    }

    private lateinit var emptyDataView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Retrieve and inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVehiclesViewModel()

        emptyDataView = view.findViewById(R.id.empty_data_parent)

        setupRecyclerView(view)

        binding.newVehicleFab.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToVehicleRegistrationFragment(
                getString(R.string.register_vehicle)
            )
            this.findNavController().navigate(action)
        }
    }

    private fun setupRecyclerView(view: View) {
        val vehicleListAdapter = VehicleListAdapter {
            val action = HomeFragmentDirections.actionHomeFragmentToVehicleDetailsFragment(it.id)
            view.findNavController().navigate(action)
        }

        binding.vehiclesListRecyclerView.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = vehicleListAdapter
        }

        vehiclesViewModel.getAllVehicles().observe(viewLifecycleOwner) { vehicles ->
            vehicleListAdapter.submitList(vehicles)
            val emptyDataObserver = EmptyDataObserver(binding.vehiclesListRecyclerView, emptyDataView)
            vehicleListAdapter.registerAdapterDataObserver(emptyDataObserver)
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