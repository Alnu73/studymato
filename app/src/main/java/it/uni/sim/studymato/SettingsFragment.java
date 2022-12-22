package it.uni.sim.studymato;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class SettingsFragment extends PreferenceFragmentCompat {

    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences.Editor sharedPreferencesEditor =
                PreferenceManager.getDefaultSharedPreferences(requireContext()).edit();
        SwitchPreferenceCompat switchOnboardingPref = findPreference("onboarding");
        SwitchPreferenceCompat switchNotificationPref = findPreference("notifications");
        Preference delAccountPref = findPreference("logout");
        Preference logoutPref = findPreference("delete_account");
        Preference tomatoDurationPref = findPreference("studytomato");
        Preference breakDurationPref = findPreference("breaktomato");

        Objects.requireNonNull(switchOnboardingPref).setOnPreferenceChangeListener((preference, newValue) -> {
            sharedPreferencesEditor.putBoolean(getString(R.string.onboarding_completed), (Boolean) newValue);
            sharedPreferencesEditor.apply();
            return true;
        });

        Objects.requireNonNull(switchNotificationPref).setOnPreferenceChangeListener((preference, newValue) -> {
            sharedPreferencesEditor.putBoolean(getString(R.string.disable_notifications), (Boolean) newValue);
            sharedPreferencesEditor.apply();
            return true;
        });

        Objects.requireNonNull(delAccountPref).setOnPreferenceClickListener(preference -> {
            if (user != null) {
                user.delete()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d("SessFragDel", "User account deleted.");
                            }
                        });
            }
            return true;
        });

        Objects.requireNonNull(logoutPref).setOnPreferenceClickListener(preference -> {
            FirebaseAuth.getInstance().signOut();
            return true;
        });

        Objects.requireNonNull(tomatoDurationPref).setOnPreferenceChangeListener((preference, newValue) -> {
            String newValueStr = String.valueOf(newValue);
            sharedPreferencesEditor.putLong(getString(R.string.study_duration), Long.parseLong(newValueStr)).apply();
            return true;
        });

        Objects.requireNonNull(breakDurationPref).setOnPreferenceChangeListener((preference, newValue) -> {
            String newValueStr = String.valueOf(newValue);
            sharedPreferencesEditor.putLong(getString(R.string.break_duration), Long.parseLong(newValueStr)).apply();
            return true;
        });

        toggleOnboardingSetting(switchOnboardingPref);

    }

    private void toggleOnboardingSetting(SwitchPreferenceCompat pref) {
        SharedPreferences onbPref = PreferenceManager.getDefaultSharedPreferences(requireContext());
        if (onbPref.getBoolean(getString(R.string.onboarding_completed), true)) {
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