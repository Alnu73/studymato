package it.uni.sim.studymato;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import java.util.Objects;

import it.uni.sim.studymato.onboarding.OnboardingPageFragment;

public class SettingsFragment extends PreferenceFragmentCompat {

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwitchPreferenceCompat pref = findPreference("onboarding");
        Objects.requireNonNull(pref).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                SharedPreferences.Editor sharedPreferencesEditor =
                        PreferenceManager.getDefaultSharedPreferences(requireContext()).edit();
                sharedPreferencesEditor.putBoolean(
                        OnboardingPageFragment.COMPLETED_ONBOARDING
                        , pref.isChecked());
                sharedPreferencesEditor.apply();
                return true;
            }
        });
        SharedPreferences onbPref = PreferenceManager.getDefaultSharedPreferences(requireContext());
        if (onbPref.getBoolean(OnboardingPageFragment.COMPLETED_ONBOARDING, true)) {
            pref.setChecked(false);
        }

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