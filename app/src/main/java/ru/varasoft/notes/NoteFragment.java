package ru.varasoft.notes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteFragment extends Fragment {

    public static final String ARG_NOTE = "note";
    private Note note;

    public NoteFragment() {
        // Required empty public constructor
    }

    public static NoteFragment newInstance(Note note) {
        NoteFragment fragment = new NoteFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_NOTE, note);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            note = getArguments().getParcelable(ARG_NOTE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        TextView titleTextView = view.findViewById(R.id.title_text_view);
        titleTextView.setText(String.format("%s %s", getResources().getString(R.string.title), note.getTitle()));

        TextView creatorTextView = view.findViewById(R.id.creator_text_view);
        creatorTextView.setText(String.format("%s %s", getResources().getString(R.string.creator), note.getCreator()));

        SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy hh:mm");

        TextView creationDateTimeTextView = view.findViewById(R.id.creation_date_time_text_view);
        String strDt = simpleDate.format(note.getCreationDateTime());
        creationDateTimeTextView.setText(String.format("%s %s", getResources().getString(R.string.creation_date_time), strDt));

        TextView modificationDateTimeTextView = view.findViewById(R.id.modification_date_time_text_view);
        strDt = simpleDate.format(note.getModificationDateTime());
        modificationDateTimeTextView.setText(String.format("%s %s", getResources().getString(R.string.modification_date_time), strDt));

        TextView noteTextView = view.findViewById(R.id.note_text_view);
        noteTextView.setText(note.getText());
        return view;
    }
}