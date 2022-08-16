package com.pkndegwa.mycarmaintenance.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pkndegwa.mycarmaintenance.CarMaintenanceApplication
import com.pkndegwa.mycarmaintenance.R
import com.pkndegwa.mycarmaintenance.adapter.VehicleListAdapter
import com.pkndegwa.mycarmaintenance.databinding.FragmentHomeBinding
import com.pkndegwa.mycarmaintenance.ui.VehiclesViewModel
import com.pkndegwa.mycarmaintenance.ui.VehiclesViewModelFactory

/**
 * [HomeFragment] allows a user to click a button to register a vehicle.
 */
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: VehiclesViewModel by activityViewModels {
        VehiclesViewModelFactory((activity?.application as CarMaintenanceApplication).database.vehicleDao())
    }

    private lateinit var vehicleListAdapter: VehicleListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Retrieve and inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        viewModel.allVehicles.observe(this.viewLifecycleOwner) { vehicles ->
            vehicleListAdapter.submitList(vehicles)
        }

        binding.newVehicleFab.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToVehicleRegistrationFragment(
                getString(R.string.register_vehicle)
            )
            this.findNavController().navigate(action)
        }
    }

    private fun setupRecyclerView() {
        vehicleListAdapter = VehicleListAdapter {
            val action = HomeFragmentDirections.actionHomeFragmentToVehicleDetailsFragment(it.id)
            this.findNavController().navigate(action)
        }

        binding.vehiclesListRecyclerView.apply {
            adapter = vehicleListAdapter
            layoutManager = LinearLayoutManager(this.context)
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