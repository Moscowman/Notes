package ru.varasoft.notes.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class NotesSourceImpl implements NotesSource, Parcelable {
    private List<Note> dataSource;

    public NotesSourceImpl() {
        dataSource = new ArrayList<>(7);
    }

    protected NotesSourceImpl(Parcel in) {
        dataSource = in.createTypedArrayList(Note.CREATOR);
    }

    public static final Creator<NotesSourceImpl> CREATOR = new Creator<NotesSourceImpl>() {
        @Override
        public NotesSourceImpl createFromParcel(Parcel in) {
            return new NotesSourceImpl(in);
        }

        @Override
        public NotesSourceImpl[] newArray(int size) {
            return new NotesSourceImpl[size];
        }
    };

    public NotesSourceImpl init(){
        dataSource.add(new Note("Заметка 1", "Траляля", "я"));
        dataSource.add(new Note("Заметка 2", "Это заметка", "не я"));
        dataSource.add(new Note("Заметка 3", "И это заметка", "мы"));
        dataSource.add(new Note("Заметка 4", "А это - нет", "они"));
        return this;
    }

    @Override
    public Note getNoteData(int position) {
        return dataSource.get(position);
    }

    @Override
    public int size(){
        return dataSource.size();
    }

    public void deleteNote(int position) {
        dataSource.remove(position);
    }

    public void updateNote(int position, Note note) {
        dataSource.remove(position);
        dataSource.add(position, note);
    }

    @Override
    public void addNote(Note note) {
        dataSource.add(note);
    }

    @Override
    public void clearNoteData() {
        dataSource.clear();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelableList(dataSource, flags);
    }
}
