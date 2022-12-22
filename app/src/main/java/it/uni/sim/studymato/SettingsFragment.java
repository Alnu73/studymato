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

import it.uni.sim.studymato.onboarding.OnboardingPageFragment;

public class SettingsFragment extends PreferenceFragmentCompat {
    //TODO: put preferences in strings.xml

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences.Editor sharedPreferencesEditor =
                PreferenceManager.getDefaultSharedPreferences(requireContext()).edit();
        SwitchPreferenceCompat switchPref = findPreference("onboarding");
        Preference delAccountPref = findPreference("logout");
        Preference logoutPref = findPreference("delete_account");
        Preference tomatoDurationPref = findPreference("studytomato");
        Preference breakDurationPref = findPreference("breaktomato");

        Objects.requireNonNull(switchPref).setOnPreferenceChangeListener((preference, newValue) -> {
            sharedPreferencesEditor.putBoolean(getString(R.string.onboarding_completed), switchPref.isChecked());
            sharedPreferencesEditor.apply();
            return true;
        });

        Objects.requireNonNull(delAccountPref).setOnPreferenceClickListener(preference -> {
            user.delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("SessFragDel", "User account deleted.");
                        }
                    });
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

        //TODO: Disable notifications
        toggleOnboardingSetting(switchPref);

    }

    private void toggleOnboardingSetting(SwitchPreferenceCompat pref) {
        SharedPreferences onbPref = PreferenceManager.getDefaultSharedPreferences(requireContext());
        System.out.println(onbPref.getLong(getString(R.string.study_duration), 0));
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