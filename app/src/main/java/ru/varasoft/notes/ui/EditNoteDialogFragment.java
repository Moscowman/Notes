package ru.varasoft.notes.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;

import ru.varasoft.notes.R;
import ru.varasoft.notes.data.OnEditNoteDialogListener;

public class EditNoteDialogFragment extends DialogFragment {
    View contentView;
    TextInputEditText titleTextInputEditText;
    TextInputEditText creatorTextView;
    TextInputEditText noteTextView;

    OnEditNoteDialogListener dialogListener;

    public void setOnDialogListener(OnEditNoteDialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    public void setFields(Bundle savedInstanceState) {
        titleTextInputEditText.setText(savedInstanceState.getString("title"));
        creatorTextView.setText(savedInstanceState.getString("creator"));
        noteTextView.setText(savedInstanceState.getString("text"));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        titleTextInputEditText = contentView.findViewById(R.id.dialog_title_text_input_edit_text);
        creatorTextView = contentView.findViewById(R.id.dialog_creator_text_input_edit_text);
        noteTextView = contentView.findViewById(R.id.dialog_note_text_input_edit_text);
        Bundle args = getArguments();
        if (args != null) {
            setFields(args);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        contentView = requireActivity().getLayoutInflater().inflate(R.layout.edit_note_dialog_custom, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity())
                .setTitle(R.string.title_edit_dialog)
                .setView(contentView)
                .setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                        Bundle bundle = new Bundle();
                        bundle.putString("title", titleTextInputEditText.getText().toString());
                        bundle.putString("creator", creatorTextView.getText().toString());
                        bundle.putString("text", noteTextView.getText().toString());
                        if (dialogListener != null) dialogListener.onDialogUpdate(bundle);
                    }
                })
                .setCancelable(true)
                .setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                        if (dialogListener != null) dialogListener.onDialogCancel();
                    }
                });
        return builder.create();
    }
}
