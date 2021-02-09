package ru.varasoft.notes.ui;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import ru.varasoft.notes.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}