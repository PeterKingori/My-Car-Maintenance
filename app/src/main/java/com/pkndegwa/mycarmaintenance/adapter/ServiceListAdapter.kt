package com.pkndegwa.mycarmaintenance.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pkndegwa.mycarmaintenance.R
import com.pkndegwa.mycarmaintenance.databinding.ServiceListItemBinding
import com.pkndegwa.mycarmaintenance.models.Service

class ServiceListAdapter : ListAdapter<Service, ServiceListAdapter.ServiceViewHolder>(DiffCallback) {
    private lateinit var context: Context

    /**
     * Provides a reference for the views needed to display items in the list.
     */
    class ServiceViewHolder(private var binding: ServiceListItemBinding) : RecyclerView.ViewHolder(binding.root) {
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
        }
    }

    /**
     * Creates new views with R.layout.service_list_item as its template.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        context = parent.context
        val layoutInflater = ServiceListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServiceViewHolder(layoutInflater)
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