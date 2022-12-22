package it.uni.sim.studymato;

import static java.lang.Math.abs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import it.uni.sim.studymato.databinding.FragmentStudySessionBinding;
import it.uni.sim.studymato.model.Exam;
import it.uni.sim.studymato.model.StudySession;

public class StudySessionFragment extends Fragment {

    FragmentStudySessionBinding binding = null;
    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference ref = mDatabase.getReference();
    private final static AtomicInteger incrId = new AtomicInteger(0);

    private CountDownTimer tomatoTimer;
    private long studyInterval;
    private long breakInterval;
    private long timeElapsed;
    private StudyIntervals currentInterval;
    private int numberOfStudyIntervals = 0;
    private enum StudyIntervals {
        STUDY,
        BREAK
    }

    public StudySessionFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(requireContext());
        binding = FragmentStudySessionBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        toggleBottomNavigationView();
        studyInterval = TimeUnit.MINUTES.toMillis(spref.getLong(getString(R.string.study_duration), 10000));
        breakInterval = TimeUnit.MINUTES.toMillis(spref.getLong(getString(R.string.break_duration), 5000));
        currentInterval = StudyIntervals.STUDY;
        binding.breakAndResumeButton.setEnabled(false);
        setupTimer(studyInterval);
        tomatoTimer.start();

        binding.breakAndResumeButton.setOnClickListener(v -> {
            goToNextInterval();
            tomatoTimer.start();
        });

        binding.endSessionButton.setOnClickListener(v -> {
            //Dialog
            tomatoTimer.cancel();
            showEndDialog();
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        OnBackPressedCallback callback = new OnBackPressedCallback(
                true // default to enabled
        ) {
            @Override
            public void handleOnBackPressed() {
                showCancelDialog();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(
                this, callback);
    }

    private void setupTimer(long interval) {
        tomatoTimer = new CountDownTimer(interval, 1000) {
            @Override
            public void onTick(long l) {
                long seconds = l/1000;
                String formattedTime = String.format(Locale.getDefault(), "%02d:%02d:%02d", seconds / 3600,
                        (seconds % 3600) / 60, (seconds % 60));
                binding.timerTextView.setText(formattedTime);
                if (currentInterval == StudyIntervals.STUDY) {
                    timeElapsed = abs(studyInterval - l);
                }
            }

            @Override
            public void onFinish() {
                if (currentInterval == StudyIntervals.STUDY) {
                    numberOfStudyIntervals += 1;
                    long mins = numberOfStudyIntervals*(studyInterval);
                    binding.progressTextView.setText(String.format("You studied %d minutes", mins));
                    binding.statusTextView.setText("Your timer has ended! You may take a break.");
                    fireIntervalNotification("You finished a study interval", "Take a break!");
                }
                else {
                    binding.statusTextView.setText("Time to resume!");
                    fireIntervalNotification("You finished a break interval", "Time to resume!");
                }
                binding.breakAndResumeButton.setEnabled(true);
            }
        };
    }

    private void goToNextInterval() {
        binding.breakAndResumeButton.setEnabled(false);
        //TODO: Send notification
        if (currentInterval == StudyIntervals.STUDY) {
            currentInterval = StudyIntervals.BREAK;
            setupTimer(breakInterval);
            binding.breakAndResumeButton.setText("Resume");
        }
        else {
            currentInterval = StudyIntervals.STUDY;
            setupTimer(studyInterval);
            binding.breakAndResumeButton.setText("Take a break");
        }
    }

    private void showCancelDialog() {
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setMessage("Your progress will be lost! Are you sure you want to exit?")
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    tomatoTimer.cancel();
                    closeWindow();
                })
                .setNegativeButton("No", ((dialogInterface, i) -> dialogInterface.cancel()))
                .create();
        dialog.show();
    }

    private void showEndDialog() {
        final long duration = numberOfStudyIntervals != 0 ? numberOfStudyIntervals*studyInterval : timeElapsed;
        System.out.println(duration);

        final Spinner spinner = new Spinner(getContext());
        ArrayList<Exam> spinnerArray = new ArrayList<Exam>();
        DatabaseReference examsRef = ref.child("exams");
        Query q = examsRef.orderByChild("name");
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Exam exam = ds.getValue(Exam.class);
                    if (exam != null) {
                        spinnerArray.add(exam);
                        ArrayAdapter<Exam> adapter = new ArrayAdapter<Exam>(getContext(), android.R.layout.simple_spinner_item, spinnerArray);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setMessage("What exam have you studied for?")
                .setView(spinner)
                .setPositiveButton("OK", (dialogInterface, i) -> {
                    if (duration != 0) {
                        Exam selectedExam = (Exam) spinner.getSelectedItem();
                        StudySession studySession = new StudySession(selectedExam, System.currentTimeMillis(), duration);
                        saveSession(studySession);
                    }
                    else {
                        Toast.makeText(getContext(), "Duration is zero. Session won't be saved.", Toast.LENGTH_SHORT).show();
                    }
                    closeWindow();
                })
                .create();
        dialog.show();
    }

    private void saveSession(StudySession session) {
        DatabaseReference sessionsRef = ref.child("sessions");
        DatabaseReference saveRef = sessionsRef.push();
        saveRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()) {
                    saveRef.setValue(session);
                }
                else {
                    Log.d("db", "Data already exists! Insertion has been canceled");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fireIntervalNotification(String textTitle, String textContent) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(requireContext());
        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), getString(R.string.channel_id))
                .setSmallIcon(R.drawable.ic_notification_icon_foreground)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        notificationManager.notify(incrId.incrementAndGet(), builder.build());
    }

    private void closeWindow() {
        requireActivity().getSupportFragmentManager().popBackStack();
        toggleBottomNavigationView();
    }

    private void toggleBottomNavigationView() {
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);
        if(bottomNavigationView.getVisibility() == View.VISIBLE)
            bottomNavigationView.setVisibility(View.GONE);
        else
            bottomNavigationView.setVisibility(View.VISIBLE);
    }
}