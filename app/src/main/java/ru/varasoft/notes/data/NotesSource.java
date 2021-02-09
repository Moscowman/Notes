package ru.varasoft.notes.data;

public interface NotesSource {
    Note getNoteData(int position);

    int size();
}
