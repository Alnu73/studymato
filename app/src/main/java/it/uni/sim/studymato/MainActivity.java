package it.uni.sim.studymato;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Date;

import it.uni.sim.studymato.authentication.AuthenticationActivity;
import it.uni.sim.studymato.databinding.ActivityMainBinding;
import it.uni.sim.studymato.model.Exam;
import it.uni.sim.studymato.onboarding.OnboardingActivity;
import it.uni.sim.studymato.onboarding.OnboardingPageFragment;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;
    ActivityMainBinding binding = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mAuth = FirebaseAuth.getInstance();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.main_nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);

        checkAndStartOnboarding();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null) {
            Intent intent = new Intent(MainActivity.this, AuthenticationActivity.class);
            startActivity(intent);
        }
//        SharedPreferences.Editor sharedPreferencesEditor =
//                PreferenceManager.getDefaultSharedPreferences(this).edit();
//        sharedPreferencesEditor.putBoolean(getString(R.string.disable_notifications), false).apply();
//        sharedPreferencesEditor.putBoolean(getString(R.string.onboarding_completed), false).apply();
        createNotificationChannel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void checkAndStartOnboarding() {
        // Check if we need to display our OnboardingSupportFragment
        if (!sharedPreferences.getBoolean(getString(R.string.onboarding_completed), false)) {
            // The user hasn't seen the OnboardingSupportFragment yet, so show it
            startActivity(new Intent(this, OnboardingActivity.class));
        }
    }

    private void createNotificationChannel() {
        boolean notificationsDisabled = sharedPreferences.getBoolean(getString(R.string.disable_notifications), false);
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        System.out.println("dis?" + notificationsDisabled);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = notificationsDisabled ? NotificationManager.IMPORTANCE_LOW : NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(getString(R.string.channel_id), name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}