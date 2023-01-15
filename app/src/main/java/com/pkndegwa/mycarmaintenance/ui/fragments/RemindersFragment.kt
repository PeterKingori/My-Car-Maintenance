package com.pkndegwa.mycarmaintenance.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pkndegwa.mycarmaintenance.CarMaintenanceApplication
import com.pkndegwa.mycarmaintenance.R
import com.pkndegwa.mycarmaintenance.adapter.ReminderListAdapter
import com.pkndegwa.mycarmaintenance.databinding.FragmentRemindersBinding
import com.pkndegwa.mycarmaintenance.utils.EmptyDataObserver
import com.pkndegwa.mycarmaintenance.viewmodels.RemindersViewModel
import com.pkndegwa.mycarmaintenance.viewmodels.createFactory

/**
 * Use the [RemindersFragment] to show a list of reminders.
 */
class RemindersFragment : Fragment() {
    private var _binding: FragmentRemindersBinding? = null
    private val binding get() = _binding!!

    private lateinit var remindersViewModel: RemindersViewModel
    private fun initRemindersViewModel() {
        val factory = RemindersViewModel(
            requireActivity().application,
            (activity?.application as CarMaintenanceApplication).database.reminderDao()
        )
            .createFactory()
        remindersViewModel = ViewModelProvider(this, factory)[RemindersViewModel::class.java]
    }

    private lateinit var emptyDataView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentRemindersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRemindersViewModel()

        emptyDataView = view.findViewById(R.id.empty_view_reminder_fragment)

        setupRemindersRecyclerView(view)

        binding.apply {
            emptyViewReminderFragment.welcomeText.visibility = View.GONE
            emptyViewReminderFragment.addVehicleText.text = getString(R.string.add_reminder_text)
        }

        binding.newReminderFab.setOnClickListener {
            this.findNavController().navigate(
                RemindersFragmentDirections.actionRemindersFragmentToAddReminderFragment(
                    title = getString(R.string.new_reminder)
                )
            )
        }
    }

    private fun setupRemindersRecyclerView(view: View) {
        val reminderListAdapter = ReminderListAdapter {
            val action = RemindersFragmentDirections.actionRemindersFragmentToAddReminderFragment(
                title = getString(R.string.edit_reminder),
                id = it.id
            )
            view.findNavController().navigate(action)
        }

        binding.remindersListRecyclerView.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = reminderListAdapter
        }

        remindersViewModel.getAllReminders().observe(viewLifecycleOwner) { reminders ->
            reminderListAdapter.submitList(reminders)
            val emptyDataObserver = EmptyDataObserver(binding.remindersListRecyclerView, emptyDataView)
            reminderListAdapter.registerAdapterDataObserver(emptyDataObserver)
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