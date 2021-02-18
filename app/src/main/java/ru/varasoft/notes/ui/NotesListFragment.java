package ru.varasoft.notes.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.varasoft.notes.MainActivity;
import ru.varasoft.notes.R;
import ru.varasoft.notes.data.Note;
import ru.varasoft.notes.data.NotesSource;
import ru.varasoft.notes.data.NotesSourceFirebaseImpl;
import ru.varasoft.notes.data.NotesSourceResponse;
import ru.varasoft.notes.data.Observer;
import ru.varasoft.notes.data.OnDeleteDialogListener;
import ru.varasoft.notes.data.OnEditNoteDialogListener;
import ru.varasoft.notes.data.Publisher;


public class NotesListFragment extends Fragment {

    public static final String CURRENT_NOTE = "CurrentNote";
    public static final String NOTES = "Notes";
    private NotesSource notes;
    private Note currentNote;
    private boolean isLandscape;

    RecyclerView recyclerView;
    private Navigation navigation;
    private Publisher publisher;
    private boolean moveToFirstPosition;
    NotesAdapter adapter;

    private int modificationPosition;

    public NotesListFragment() {
        // Required empty public constructor
    }

    public static NotesListFragment newInstance() {
        NotesListFragment fragment = new NotesListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList(view);
        recyclerView = view.findViewById(R.id.recycler_view_lines);
        initRecyclerView(recyclerView, notes);
        notes = new NotesSourceFirebaseImpl().init(new NotesSourceResponse() {
            @Override
            public void initialized(NotesSource notesData) {
                adapter.notifyDataSetChanged();
            }
        });
        adapter.setDataSource(notes);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MainActivity activity = (MainActivity) context;
        navigation = activity.getNavigation();
        publisher = activity.getPublisher();
    }

    @Override
    public void onDetach() {
        navigation = null;
        publisher = null;
        super.onDetach();
    }

    private void initRecyclerView(RecyclerView recyclerView, NotesSource data) {

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new NotesAdapter(this);
        recyclerView.setAdapter(adapter);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.separator, null));
        recyclerView.addItemDecoration(itemDecoration);

        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(1000);
        animator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(animator);

        if (moveToFirstPosition && data.size() > 0) {
            recyclerView.scrollToPosition(0);
            moveToFirstPosition = false;
        }
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.note_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = adapter.getMenuPosition();
        switch (item.getItemId()) {
            case R.id.action_update:
                updateNote();
                return true;
            case R.id.action_delete:
                deleteNote(position);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void deleteNote(int position) {
        modificationPosition = position;
        DeleteNoteDialogFragment dialogFragment = new DeleteNoteDialogFragment();
        dialogFragment.setOnDialogListener(deleteDialogListener);
        dialogFragment.show(requireActivity().getSupportFragmentManager(),
                "delete_dialog_fragment");
    }

    private void updateNote() {
        modificationPosition = adapter.getMenuPosition();
        currentNote = notes.getNoteData(modificationPosition);
        EditNoteDialogFragment updateNoteDialogFragment = new EditNoteDialogFragment();
        updateNoteDialogFragment.setOnDialogListener(updateNoteDialogListener);
        Bundle args = new Bundle();
        args.putString("title", currentNote.getTitle());
        args.putString("creator", currentNote.getCreator());
        args.putString("text", currentNote.getText());
        updateNoteDialogFragment.setArguments(args);
        updateNoteDialogFragment.show(requireActivity().getSupportFragmentManager(),
                "update_dialog_fragment");
    }

    private void addNote() {
        EditNoteDialogFragment addNoteDialogFragment = new EditNoteDialogFragment();
        addNoteDialogFragment.setOnDialogListener(addNoteDialogListener);
        addNoteDialogFragment.show(requireActivity().getSupportFragmentManager(),
                "add_dialog_fragment");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(CURRENT_NOTE, currentNote);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return onItemSelected(item.getItemId()) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        getActivity().getMenuInflater().inflate(R.menu.notes_list_main_menu, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchText = (SearchView) search.getActionView();
        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        isLandscape = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;

        if (savedInstanceState != null) {
            currentNote = savedInstanceState.getParcelable(CURRENT_NOTE);
        } else {
            if (notes.size() > 0) {
                currentNote = notes.getNoteData(0);
            }
        }

        if (isLandscape) {
            showLandNote(currentNote);
        }
    }

    private void initList(View view) {
        FrameLayout layoutView = (FrameLayout) view;
    }

    private boolean onItemSelected(int menuItemId) {
        switch (menuItemId) {
            case R.id.action_add:
                addNote();
                return true;
            case R.id.action_update:
                updateNote();
                return true;
            case R.id.action_delete:
                deleteNote(adapter.getMenuPosition());
                return true;
            case R.id.action_clear:
                notes.clearNoteData();
                adapter.notifyDataSetChanged();
                return true;
        }
        return false;
    }

    private void showNote(Note currentNote) {
        if (isLandscape) {
            showLandNote(currentNote);
        } else {
            showPortNote(currentNote);
        }
    }

    private void showLandNote(Note currentNote) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.replace(R.id.note_fragment, NoteFragment.newInstance(currentNote));
        fragmentTransaction.commit();
    }

    private void showPortNote(Note currentNote) {
        NoteFragment note = NoteFragment.newInstance(currentNote);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, note)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    private OnDeleteDialogListener deleteDialogListener = new OnDeleteDialogListener() {
        @Override
        public void onDialogDelete() {
            notes.deleteNote(modificationPosition);
            adapter.notifyItemRemoved(modificationPosition);
        }

        @Override
        public void onDialogCancel() {
        }
    };

    private OnEditNoteDialogListener updateNoteDialogListener = new OnEditNoteDialogListener() {
        @Override
        public void onDialogUpdate(Bundle bundle) {
            currentNote.setTitle(bundle.getString("title"));
            currentNote.setCreator(bundle.getString("creator"));
            currentNote.setText(bundle.getString("text"));
            notes.updateNote(modificationPosition, currentNote);
            adapter.notifyItemChanged(modificationPosition);
        }

        @Override
        public void onDialogCancel() {
        }
    };

    private OnEditNoteDialogListener addNoteDialogListener = new OnEditNoteDialogListener() {
        @Override
        public void onDialogUpdate(Bundle bundle) {
            currentNote = new Note(bundle.getString("title"), bundle.getString("text"), bundle.getString("creator"));
            notes.addNote(currentNote);
            adapter.notifyItemInserted(notes.size() - 1);
            moveToFirstPosition = true;
        }

        @Override
        public void onDialogCancel() {
        }
    };

}