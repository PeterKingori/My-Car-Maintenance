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
import com.pkndegwa.mycarmaintenance.adapter.NoteListAdapter
import com.pkndegwa.mycarmaintenance.databinding.FragmentNotesBinding
import com.pkndegwa.mycarmaintenance.utils.EmptyDataObserver
import com.pkndegwa.mycarmaintenance.viewmodels.NotesViewModel
import com.pkndegwa.mycarmaintenance.viewmodels.createFactory

class NotesFragment : Fragment() {
    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    private lateinit var notesViewModel: NotesViewModel
    private fun initNotesViewModel() {
        val factory =
            NotesViewModel((activity?.application as CarMaintenanceApplication).database.noteDao()).createFactory()
        notesViewModel = ViewModelProvider(this, factory)[NotesViewModel::class.java]
    }

    private lateinit var emptyDataView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initNotesViewModel()

        emptyDataView = view.findViewById(R.id.empty_view_notes_fragment)

        setUpNotesRecyclerView(view)

        binding.apply {
            emptyViewNotesFragment.welcomeText.visibility = View.GONE
            emptyViewNotesFragment.addVehicleText.text = getString(R.string.add_note_text)
        }

        binding.newNoteFab.setOnClickListener {
            this.findNavController().navigate(
                NotesFragmentDirections.actionNotesFragmentToAddNoteFragment(
                    title = getString(R.string.add_new_note)
                )
            )
        }
    }

    private fun setUpNotesRecyclerView(view: View) {
        val noteListAdapter = NoteListAdapter {
            val action = NotesFragmentDirections.actionNotesFragmentToAddNoteFragment(
                title = "Edit Note",
                id = it.id
            )
            view.findNavController().navigate(action)
        }

        binding.notesListRecyclerView.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = noteListAdapter
        }

        notesViewModel.getAllNotes().observe(viewLifecycleOwner) { notes ->
            noteListAdapter.submitList(notes)
            val emptyDataObserver = EmptyDataObserver(binding.notesListRecyclerView, emptyDataView)
            noteListAdapter.registerAdapterDataObserver(emptyDataObserver)
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