package com.pkndegwa.mycarmaintenance.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.pkndegwa.mycarmaintenance.adapter.ManufacturerAdapter
import com.pkndegwa.mycarmaintenance.data.ManufacturerData
import com.pkndegwa.mycarmaintenance.databinding.FragmentVehicleManufacturersBinding

/**
 * [VehicleManufacturersFragment] allows a user to select a manufacturer when registering their vehicle.
 */
class VehicleManufacturersFragment : Fragment() {
    private var _binding: FragmentVehicleManufacturersBinding? = null
    private lateinit var recyclerView: RecyclerView

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Retrieve and inflate the layout for this fragment
        _binding = FragmentVehicleManufacturersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val manufacturersDataset = ManufacturerData.getManufacturerData()
        recyclerView = binding.recyclerView
        recyclerView.adapter = ManufacturerAdapter(this.requireContext(), manufacturersDataset)

        // Adds a [DividerItemDecoration] between items
        recyclerView.addItemDecoration(
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        )
    }

    /**
     * Frees the binding object when the Fragment is destroyed.
     */
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}