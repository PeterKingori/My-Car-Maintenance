package com.pkndegwa.mycarmaintenance.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pkndegwa.mycarmaintenance.CarMaintenanceApplication
import com.pkndegwa.mycarmaintenance.R
import com.pkndegwa.mycarmaintenance.databinding.FragmentAddNoteBinding
import com.pkndegwa.mycarmaintenance.models.Note
import com.pkndegwa.mycarmaintenance.utils.isEntryValid
import com.pkndegwa.mycarmaintenance.viewmodels.NotesViewModel
import com.pkndegwa.mycarmaintenance.viewmodels.createFactory

/**
 * Use the [AddNoteFragment] to add or update a note.
 */
class AddNoteFragment : Fragment() {
    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!

    private lateinit var notesViewModel: NotesViewModel
    private fun initNotesViewModel() {
        val factory =
            NotesViewModel((activity?.application as CarMaintenanceApplication).database.noteDao()).createFactory()
        notesViewModel = ViewModelProvider(this, factory)[NotesViewModel::class.java]
    }

    private val navigationArgs: AddNoteFragmentArgs by navArgs()
    private lateinit var note: Note

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initNotesViewModel()

        val noteId = navigationArgs.id
        if (noteId > 0) {
            notesViewModel.retrieveNote(noteId).observe(this.viewLifecycleOwner) { selectedNote ->
                note = selectedNote
                bindDetails(note)
            }

            val menuHost: MenuHost = requireActivity()
            menuHost.addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menu.clear()
                    menuInflater.inflate(R.menu.delete_menu_item, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.delete_reminder -> {
                            showConfirmationDialogForNote()
                            true
                        }
                        else -> false
                    }
                }

            }, viewLifecycleOwner, Lifecycle.State.RESUMED)
        } else {
            binding.saveNoteButton.setOnClickListener {
                addNewNote()
            }
        }
    }

    /**
     * Validates user input before adding a new note in the database using the ViewModel.
     */
    private fun addNewNote() {
        if (isEntryValid(binding.noteTitle) && isEntryValid(binding.noteBody)) {
            val result = notesViewModel.addNewNote(
                noteTitle = this.binding.noteTitleEditText.text.toString(),
                noteBody = this.binding.noteBodyEditText.text.toString()
            )
            if (result) {
                Toast.makeText(this.context, "Note saved successfully", Toast.LENGTH_SHORT).show()
                this.findNavController().navigate(AddNoteFragmentDirections.actionAddNoteFragmentToNotesFragment())
            }
        }
    }

    /**
     * Binds the note data to the TextViews when a note is selected in the NotesFragment.
     */
    private fun bindDetails(note: Note) {
        binding.apply {
            noteTitleEditText.setText(note.noteTitle, TextView.BufferType.SPANNABLE)
            noteBodyEditText.setText(note.noteBody, TextView.BufferType.SPANNABLE)

            saveNoteButton.setOnClickListener { updateNote() }
        }
    }

    /**
     * Validates user input before updating the note details in the database using the ViewModel.
     */
    private fun updateNote() {
        if (isEntryValid(binding.noteTitle) && isEntryValid(binding.noteBody)) {
            val result = notesViewModel.updateNote(
                noteId = this.navigationArgs.id,
                noteTitle = this.binding.noteTitleEditText.text.toString(),
                noteBody = this.binding.noteBodyEditText.text.toString()
            )
            if (result) {
                Toast.makeText(this.context, "Note updated successfully", Toast.LENGTH_SHORT).show()
                this.findNavController().navigate(AddNoteFragmentDirections.actionAddNoteFragmentToNotesFragment())
            }
        }
    }

    /**
     * Displays an alert dialog to get the user's confirmation before deleting the reminder.
     */
    private fun showConfirmationDialogForNote() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage("Are you sure you want to delete the note?")
            .setCancelable(false)
            .setNegativeButton(getString(R.string.cancel), null)
            .setPositiveButton(getString(R.string.delete)) { _, _ ->
                deleteNote()
            }
            .show()
    }

    private fun deleteNote() {
        notesViewModel.deleteNote(note)
        this.findNavController().navigate(AddNoteFragmentDirections.actionAddNoteFragmentToNotesFragment())
    }

    /**
     * Frees the binding object when the Fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}