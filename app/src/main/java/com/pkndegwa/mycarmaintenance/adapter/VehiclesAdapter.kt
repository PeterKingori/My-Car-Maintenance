package com.pkndegwa.mycarmaintenance.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pkndegwa.mycarmaintenance.databinding.VehiclesListItemBinding
import com.pkndegwa.mycarmaintenance.model.Vehicle

/**
 * Adapter for the [RecyclerView] in VehiclesFragment.
 */
class VehiclesAdapter(private val dataset: List<Vehicle>) : RecyclerView.Adapter<VehiclesAdapter.VehiclesViewHolder>() {

    /**
     * Provides a reference for the views needed to display items in the list.
     */
    class VehiclesViewHolder(private var binding: VehiclesListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(vehicle: Vehicle) {
            binding.vehicleModel.text = vehicle.model
            binding.vehicleManufacturer.text = vehicle.manufacturer
            binding.vehicleLicense.text = vehicle.licensePlate
            binding.vehicleOdometer.text = vehicle.mileage.toString()
        }
    }

    /**
     * Creates new views with R.layout.vehicles_list_item as its template.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehiclesViewHolder {
        val layoutInflater = VehiclesListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VehiclesViewHolder(layoutInflater)
    }

    /**
     * Replaces the content of an existing view with new data.
     */
    override fun onBindViewHolder(holder: VehiclesViewHolder, position: Int) {
        val vehicle = dataset[position]
        holder.bind(vehicle)
    }

    /**
     * Returns the size of the dataset (invoked by the layout manager).
     */
    override fun getItemCount(): Int = dataset.size
}