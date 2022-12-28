package com.pkndegwa.mycarmaintenance.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pkndegwa.mycarmaintenance.databinding.FileImageListItemBinding

class FileImageListAdapter :
    ListAdapter<String, FileImageListAdapter.FileImageViewHolder>(DiffCallback) {

    class FileImageViewHolder(private val binding: FileImageListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imageUri: String) {
            val receiptImageUri = Uri.parse(imageUri)
            Glide.with(binding.receiptImage)
                .load(receiptImageUri)
                .centerCrop()
                .into(binding.receiptImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileImageViewHolder {
        val layoutInflater = FileImageListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FileImageViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: FileImageViewHolder, position: Int) {
        val currentImage = getItem(position)
        holder.bind(currentImage)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

        }
    }
}