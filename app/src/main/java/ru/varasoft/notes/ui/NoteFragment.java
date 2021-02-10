package ru.varasoft.notes.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;

import ru.varasoft.notes.MainActivity;
import ru.varasoft.notes.data.Note;
import ru.varasoft.notes.R;
import ru.varasoft.notes.data.Publisher;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteFragment extends Fragment {

    public static final String ARG_NOTE = "note";

    private Note note;
    private Publisher publisher;

    TextInputEditText titleTextInputEditText;
    TextInputEditText creatorTextView;
    SimpleDateFormat simpleDate;
    TextView creationDateTimeTextView;
    TextView modificationDateTimeTextView;
    TextInputEditText noteTextView;

    public NoteFragment() {
        note = new Note("", "", "");
    }

    public static NoteFragment newInstance(Note note) {
        NoteFragment fragment = new NoteFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_NOTE, note);
        fragment.setArguments(args);
        return fragment;
    }

    public static NoteFragment newInstance() {
        NoteFragment fragment = new NoteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            note = getArguments().getParcelable(ARG_NOTE);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity)context;
        publisher = activity.getPublisher();
    }

    @Override
    public void onDetach() {
        publisher = null;
        super.onDetach();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        titleTextInputEditText = view.findViewById(R.id.title_text_input_edit_text);
        titleTextInputEditText.setText(note.getTitle());

        creatorTextView = view.findViewById(R.id.creator_text_input_edit_text);
        creatorTextView.setText(note.getCreator());

        simpleDate = new SimpleDateFormat("dd/MM/yyyy hh:mm");

        creationDateTimeTextView = view.findViewById(R.id.creation_date_time_text_view);
        String stringOfDate = simpleDate.format(note.getCreationDateTime());
        creationDateTimeTextView.setText(String.format("%s %s", getResources().getString(R.string.creation_date_time), stringOfDate));

        modificationDateTimeTextView = view.findViewById(R.id.modification_date_time_text_view);
        stringOfDate = simpleDate.format(note.getModificationDateTime());
        modificationDateTimeTextView.setText(String.format("%s %s", getResources().getString(R.string.modification_date_time), stringOfDate));

        noteTextView = view.findViewById(R.id.note_text_input_edit_text);
        noteTextView.setText(note.getText());
        return view;
    }


    @Override
    public void onStop() {
        super.onStop();
        note = collectCardData();
    }

    // Здесь передадим данные в паблишер
    @Override
    public void onDestroy() {
        super.onDestroy();
        publisher.notifySingle(note);
    }

    private Note collectCardData(){
        String title = this.titleTextInputEditText.getText().toString();
        String creator = this.creatorTextView.getText().toString();
        String text = this.noteTextView.getText().toString();
        return new Note(title, creator, text);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.note_main_menu, menu);
        MenuItem share = menu.findItem(R.id.action_share);
        share.setOnMenuItemClickListener( new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
    }
}