package com.pkndegwa.mycarmaintenance.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.pkndegwa.mycarmaintenance.database.NoteDao
import com.pkndegwa.mycarmaintenance.models.Note
import kotlinx.coroutines.launch

class NotesViewModel(private val noteDao: NoteDao) : ViewModel() {

    fun getAllNotes() = noteDao.getAllNotes()

    /**
     * This method adds a [Note] object to the database on a background thread.
     * @param [note]
     * @return Boolean
     */
    private fun insertNote(note: Note): Boolean {
        return try {
            viewModelScope.launch { noteDao.addNote(note) }
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Converts note details that have been entered by a user to a new [Note] instance and returns it.
     * @return Note
     */
    private fun createNewNoteEntry(noteTitle: String, noteBody: String): Note {
        return Note(noteTitle = noteTitle, noteBody = noteBody)
    }

    /**
     * Public function that takes in note data, gets a new [Note] instance,
     * and passes the information to [insertNote] to be saved to the database.
     * @return Boolean
     */
    fun addNewNote(noteTitle: String, noteBody: String): Boolean {
        val newNote = createNewNoteEntry(noteTitle, noteBody)
        return insertNote(newNote)
    }

    /**
     * This function retrieves the note details from the database based on the [id].
     * @return LiveData<Note>
     */
    fun retrieveNote(id: Int): LiveData<Note> {
        return noteDao.getNote(id).asLiveData()
    }

    /**
     * This function deletes a [Note] object from the database on a background thread.
     */
    fun deleteNote(note: Note) {
        viewModelScope.launch { noteDao.deleteNote(note) }
    }

    /**
     * This function takes in a [Note] object and updates the data of the existing
     * note in the database on a background thread.
     * @param [note]
     * @return Boolean
     */
    private fun updateNote(note: Note): Boolean {
        return try {
            viewModelScope.launch { noteDao.updateNote(note) }
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Converts edited note details that have been entered by the user to a [Note] instance
     * and returns it.
     * @return Note
     */
    private fun getUpdatedNoteEntry(noteId: Int, noteTitle: String, noteBody: String): Note {
        return Note(id = noteId, noteTitle = noteTitle, noteBody = noteBody)
    }

    /**
     * Public function that takes in updated note details, gets an updated [Note] instance,
     * and passes the information to [updateNote] to be updated in the database.
     * @return Boolean
     */
    fun updateNote(noteId: Int, noteTitle: String, noteBody: String): Boolean {
        val updatedNote = getUpdatedNoteEntry(noteId, noteTitle, noteBody)
        return updateNote(updatedNote)
    }
}