package com.pkndegwa.mycarmaintenance.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pkndegwa.mycarmaintenance.R
import com.pkndegwa.mycarmaintenance.model.Manufacturer

class ManufacturerAdapter(private val context: Context, private val dataset: List<Manufacturer>) :
    RecyclerView.Adapter<ManufacturerAdapter.ManufacturerViewHolder>() {

    class ManufacturerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.manufacturer_logo)
        val textView: TextView = view.findViewById(R.id.manufacturer_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManufacturerViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.manufacturers_list_item, parent, false)
        return ManufacturerViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ManufacturerViewHolder, position: Int) {
        val manufacturer = dataset[position]
        holder.imageView.setImageResource(manufacturer.logoResourceId)
        holder.textView.text = context.resources.getString(manufacturer.nameResourceId)
    }

    override fun getItemCount() = dataset.size
}