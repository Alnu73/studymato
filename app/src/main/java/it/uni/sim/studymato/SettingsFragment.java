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
    public static final String STUDY_DURATION = "study_duration";
    public static final String BREAK_DURATION = "break_duration";

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
            sharedPreferencesEditor.putBoolean(
                    OnboardingPageFragment.COMPLETED_ONBOARDING
                    , switchPref.isChecked());
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
            sharedPreferencesEditor.putLong(STUDY_DURATION, (Long) newValue);
            return true;
        });

        Objects.requireNonNull(breakDurationPref).setOnPreferenceChangeListener((preference, newValue) -> {
            sharedPreferencesEditor.putLong(BREAK_DURATION, (Long) newValue);
            return true;
        });


        //TODO: Disable notifications
        toggleOnboardingSetting(switchPref);

    }

    private void toggleOnboardingSetting(SwitchPreferenceCompat pref) {
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