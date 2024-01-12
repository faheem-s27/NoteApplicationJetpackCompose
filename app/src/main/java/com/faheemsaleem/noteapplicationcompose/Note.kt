package com.faheemsaleem.noteapplicationcompose

data class Note(val title: String = "", val subtitle: String= "", val id: Int = 0)

data class Notes(val list: List<Note>)

sealed class NoteActions{
    data class AddNote(val note: Note): NoteActions()
    data class DeleteNote(val note: Note): NoteActions()
    data object DeleteAllNotes: NoteActions()
    //data class ViewNote(val note: Note): NoteActions()
}
