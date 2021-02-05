package ru.varasoft.notes;

public interface NotesSource {
    Note getNoteData(int position);

    int size();
}
