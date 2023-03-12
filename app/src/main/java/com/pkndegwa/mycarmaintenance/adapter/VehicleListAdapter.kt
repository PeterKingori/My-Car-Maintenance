package com.pkndegwa.mycarmaintenance.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pkndegwa.mycarmaintenance.R
import com.pkndegwa.mycarmaintenance.databinding.VehiclesListItemBinding
import com.pkndegwa.mycarmaintenance.models.Vehicle
import com.pkndegwa.mycarmaintenance.utils.changeVehicleTypeImage

/**
 * This class implements a [RecyclerView] [ListAdapter] in VehiclesFragment which uses Data Binding to present [List]
 * data, including computing diffs between lists.
 */
class VehicleListAdapter(private val onItemClicked: (Vehicle) -> Unit) :
    ListAdapter<Vehicle, VehicleListAdapter.VehicleViewHolder>(DiffCallback) {
    private lateinit var context: Context

    /**
     * Provides a reference for the views needed to display items in the list.
     */
    class VehicleViewHolder(private val binding: VehiclesListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(vehicle: Vehicle, context: Context) {
            binding.apply {
                vehicleName.text = context.getString(R.string.vehicle_name, vehicle.manufacturer, vehicle.model)
                vehicleLicense.text = vehicle.licensePlate
                vehicleOdometer.text = context.getString(R.string.formatted_vehicle_mileage, vehicle.mileage)
                vehicleModelYear.text = vehicle.modelYear.toString()
            }
            val vehicleImageUri = Uri.parse(vehicle.vehicleImageUri)
            Glide.with(context)
                .load(vehicleImageUri)
                .centerCrop()
                .error(changeVehicleTypeImage(context, vehicle.type, false))
                .fallback(changeVehicleTypeImage(context, vehicle.type, false))
                .into(binding.vehicleImage)
        }
    }

    /**
     * Creates new views with R.layout.vehicles_list_item as its template.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        context = parent.context
        val layoutInflater = VehiclesListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VehicleViewHolder(layoutInflater)
    }

    /**
     * Replaces the content of an existing view with new data.
     */
    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        val currentVehicle = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(currentVehicle)
        }
        holder.bind(currentVehicle, context)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Vehicle>() {
            override fun areItemsTheSame(oldItem: Vehicle, newItem: Vehicle): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Vehicle, newItem: Vehicle): Boolean {
                return oldItem == newItem
            }
        }
    }
}