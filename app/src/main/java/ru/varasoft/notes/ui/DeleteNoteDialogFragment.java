package ru.varasoft.notes.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import ru.varasoft.notes.R;
import ru.varasoft.notes.data.OnDeleteDialogListener;

public class DeleteNoteDialogFragment extends DialogFragment {

    OnDeleteDialogListener dialogListener;

    public void setOnDialogListener(OnDeleteDialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final View contentView = requireActivity().getLayoutInflater().inflate(R.layout.delete_dialog_custom, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity())
                .setTitle(R.string.title_delete_dialog)
                .setView(contentView)
                .setPositiveButton(R.string.delete_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                        if (dialogListener != null) dialogListener.onDialogDelete();
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
