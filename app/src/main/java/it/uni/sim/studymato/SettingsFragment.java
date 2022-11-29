package it.uni.sim.studymato;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;
import it.uni.sim.studymato.databinding.FragmentSettingsBinding;

/*
-durata pomodoro
-logout
 */

public class SettingsFragment extends PreferenceFragmentCompat {

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}