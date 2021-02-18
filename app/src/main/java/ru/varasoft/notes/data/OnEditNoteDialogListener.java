package ru.varasoft.notes.data;

import android.os.Bundle;

public interface OnEditNoteDialogListener {
    void onDialogUpdate(Bundle bundle);
    void onDialogCancel();
}
