package com.example.mymultinotes.listeners;

import com.example.mymultinotes.entities.Note;

public interface NotesListener {
    void onNoteClicked(Note note, int position);
}
