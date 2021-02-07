package ru.varasoft.notes;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

public class NotesSourceImpl implements NotesSource{
    private List<Note> dataSource;

    public NotesSourceImpl() {
        dataSource = new ArrayList<>(7);
    }

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
}
