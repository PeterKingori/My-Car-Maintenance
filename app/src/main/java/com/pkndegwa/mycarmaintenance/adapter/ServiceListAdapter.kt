package com.pkndegwa.mycarmaintenance.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pkndegwa.mycarmaintenance.R
import com.pkndegwa.mycarmaintenance.databinding.ServiceListItemBinding
import com.pkndegwa.mycarmaintenance.models.Service
import com.pkndegwa.mycarmaintenance.ui.fragments.VehicleDetailsFragmentDirections

/**
 * This class implements a [RecyclerView] [ListAdapter] which uses Data Binding to present [List]
 * data, including computing diffs between lists.
 */
class ServiceListAdapter(private val onDeleteItemClicked: (Service) -> Unit) : ListAdapter<Service, ServiceListAdapter
.ServiceViewHolder>(DiffCallback) {
    private lateinit var context: Context

    /**
     * Provides a reference for the views needed to display items in the list.
     */
    class ServiceViewHolder(private val binding: ServiceListItemBinding, private val onDeleteItem: (Service) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(service: Service, context: Context) {
            binding.apply {
                servicesDoneList.text = service.servicesDoneList
                serviceDateTextView.text = service.serviceDate
                currentMileageTextView.text =
                    context.getString(R.string.formatted_vehicle_mileage, service.currentMileage)
                nextServiceMileageTextView.text =
                    context.getString(R.string.formatted_vehicle_mileage, service.nextServiceMileage)
                nextServiceDateTextView.text = service.nextServiceDate
                totalCostTextView.text = context.getString(R.string.formatted_total_cost, service.totalCost)
                serviceNotesTextView.text = service.notes

                servicesHeadingContainer.setOnClickListener {
                    if (servicesDetailsContainer.visibility == ViewGroup.GONE) {
                        servicesDetailsContainer.visibility = ViewGroup.VISIBLE
                    } else {
                        servicesDetailsContainer.visibility = ViewGroup.GONE
                    }
                }
            }
            if (service.receiptImageUriString.isNullOrEmpty()) {
                binding.receiptImageButton.visibility = View.INVISIBLE
            } else {
                binding.receiptImageButton.setOnClickListener { itemView ->
                    val action = service.receiptImageUriString.let {
                        VehicleDetailsFragmentDirections.actionVehicleDetailsFragmentToViewImageFragment(
                            title = context.getString(R.string.receipt_image_title),
                            imageUriString = it
                        )
                    }
                    itemView.findNavController().navigate(action)
                }
            }

            binding.editServiceButton.setOnClickListener { itemView ->
                val action =
                    VehicleDetailsFragmentDirections.actionVehicleDetailsFragmentToAddServiceFragment(
                        vehicleId = service.vehicleId,
                        serviceId = service.id,
                        title = context.resources.getString(R.string.edit_service)
                    )
                itemView.findNavController().navigate(action)
            }
            binding.deleteServiceButton.setOnClickListener { onDeleteItem(service) }
        }
    }

    /**
     * Creates new views with R.layout.service_list_item as its template.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        context = parent.context
        val layoutInflater = ServiceListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServiceViewHolder(layoutInflater, onDeleteItemClicked)
    }

    /**
     * Replaces the content of an existing view with new data.
     */
    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val currentService = getItem(position)
        holder.bind(currentService, context)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Service>() {
            override fun areItemsTheSame(oldItem: Service, newItem: Service): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Service, newItem: Service): Boolean {
                return oldItem == newItem
            }
        }
    }
}