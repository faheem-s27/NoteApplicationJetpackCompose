package com.faheemsaleem.noteapplicationcompose

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class NoteViewModel: ViewModel() {
    private var notes by mutableStateOf(listOf<Note>())

    var state by mutableStateOf(Notes(notes))
        private set

    fun onAction(action: NoteActions){
        when (action)
        {
            is NoteActions.AddNote -> AddNote(action.note.title, action.note.subtitle)
            is NoteActions.DeleteNote -> DeleteNote(action.note.title, action.note.subtitle)
            is NoteActions.DeleteAllNotes -> DeleteAllNotes()
        }
    }

    private fun AddNote(title: String, subtitle: String)
    {
        val note = Note(title, subtitle)
        notes = notes + note
        state = state.copy(list = notes)
    }

    fun DeleteNote(title: String, subtitle: String)
    {
        val note = Note(title, subtitle)
        notes = notes - note
        state = state.copy(list = notes)
    }

    fun DeleteAllNotes()
    {

    }

}
