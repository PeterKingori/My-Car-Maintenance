package com.pkndegwa.mycarmaintenance.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pkndegwa.mycarmaintenance.databinding.RemindersListItemBinding
import com.pkndegwa.mycarmaintenance.models.Reminder

/**
 * This class implements a [RecyclerView] [ListAdapter] in RemindersFragment which uses Data Binding to present [List]
 * data, including computing diffs between lists.
 */
class ReminderListAdapter(private val onItemClicked: (Reminder) -> Unit) :
    ListAdapter<Reminder, ReminderListAdapter.ReminderViewHolder>(DiffCallback) {

    class ReminderViewHolder(private val binding: RemindersListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(reminder: Reminder) {
            binding.apply {
                reminderInfo.text = reminder.reminderText
                dateToRemind.text = reminder.reminderDate
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val layoutInflater = RemindersListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReminderViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        val currentReminder = getItem(position)
        holder.itemView.setOnClickListener { onItemClicked(currentReminder) }
        holder.bind(currentReminder)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Reminder>() {
            override fun areItemsTheSame(oldItem: Reminder, newItem: Reminder): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Reminder, newItem: Reminder): Boolean {
                return oldItem == newItem
            }

        }
    }
}