package ru.varasoft.notes.data;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NotesSourceFirebaseImpl implements NotesSource, Parcelable {
    private static final String NOTES_COLLECTION = "notes";
    private static final String TAG = "[NotesSourceFBImpl]";

    // База данных Firestore
    private FirebaseFirestore store = FirebaseFirestore.getInstance();

    // Коллекция документов
    private CollectionReference collection = store.collection(NOTES_COLLECTION);

    private List<Note> dataSource = new ArrayList<Note>();

    public NotesSourceFirebaseImpl() {
        dataSource = new ArrayList<>(7);
    }

    protected NotesSourceFirebaseImpl(Parcel in) {
        dataSource = in.createTypedArrayList(Note.CREATOR);
    }

    public static final Creator<NotesSourceFirebaseImpl> CREATOR = new Creator<NotesSourceFirebaseImpl>() {
        @Override
        public NotesSourceFirebaseImpl createFromParcel(Parcel in) {
            return new NotesSourceFirebaseImpl(in);
        }

        @Override
        public NotesSourceFirebaseImpl[] newArray(int size) {
            return new NotesSourceFirebaseImpl[size];
        }
    };

    public NotesSourceFirebaseImpl init(NotesSourceResponse notesSourceResponse)/*Временный хардкод, нет смысла выносить в ресурсы*/ {
        collection.orderBy(NoteDataMapping.Fields.CREATION_DATE_TIME, Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    // При удачном считывании данных загрузим список карточек
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            dataSource = new ArrayList<Note>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> doc = document.getData();
                                String id = document.getId();
                                Note note = NoteDataMapping.toNote(id, doc);
                                dataSource.add(note);
                            }
                            Log.d(TAG, "success " + dataSource.size() + " qnt");
                            notesSourceResponse.initialized(NotesSourceFirebaseImpl.this);
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "get failed with ", e);
                    }
                });
        return this;

    }

    @Override
    public Note getNoteData(int position) {
        return dataSource.get(position);
    }

    @Override
    public int size() {
        if (dataSource == null) {
            return 0;
        }
        return dataSource.size();
    }

    public void deleteNote(int position) {
        collection.document(dataSource.get(position).getId()).delete();
        dataSource.remove(position);
    }

    public void updateNote(int position, Note note) {
        String id = note.getId();
        // Изменить документ по идентификатору
        collection.document(id).set(NoteDataMapping.toDocument(note));
    }

    @Override
    public void addNote(Note note) {
        collection.add(NoteDataMapping.toDocument(note)).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                note.setId(documentReference.getId());
            }
        });
    }

    @Override
    public void clearNoteData() {
        for (Note note : dataSource) {
            collection.document(note.getId()).delete();
        }
        dataSource = new ArrayList<Note>();
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
