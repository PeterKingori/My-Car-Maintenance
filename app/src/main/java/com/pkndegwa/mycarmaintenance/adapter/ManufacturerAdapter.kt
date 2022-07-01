package com.pkndegwa.mycarmaintenance.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.pkndegwa.mycarmaintenance.databinding.ManufacturersListItemBinding
import com.pkndegwa.mycarmaintenance.model.Manufacturer
import com.pkndegwa.mycarmaintenance.ui.VehicleManufacturersFragmentDirections

/**
 * Adapter for the RecyclerView in VehicleManufacturersFragment.
 */
class ManufacturerAdapter(private val context: Context, private val dataset: List<Manufacturer>) :
    RecyclerView.Adapter<ManufacturerAdapter.ManufacturerViewHolder>() {

    /**
     * Provides a reference for the views needed to display items in the list.
     */
    class ManufacturerViewHolder(binding: ManufacturersListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val viewHolder = binding.manufacturerDetails
        val imageView = binding.manufacturerLogo
        val textView = binding.manufacturerName
    }

    /**
     * Creates new views with R.layout.manufacturers_list_item as its template.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManufacturerViewHolder {
        val adapterLayout = ManufacturersListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ManufacturerViewHolder(adapterLayout)
    }

    /**
     * Replaces the content of an existing view with new data.
     */
    override fun onBindViewHolder(holder: ManufacturerViewHolder, position: Int) {
        val manufacturer = dataset[position]
        holder.imageView.setImageResource(manufacturer.logoResourceId)
        holder.textView.text = context.resources.getString(manufacturer.nameResourceId)

        holder.viewHolder.setOnClickListener { view ->
            val action =
                VehicleManufacturersFragmentDirections.actionVehicleManufacturersFragmentToVehicleRegistrationFragment(
                    manufacturerName = holder.textView.text.toString()
                )
            view.findNavController().navigate(action)
        }
    }

    override fun getItemCount() = dataset.size
}