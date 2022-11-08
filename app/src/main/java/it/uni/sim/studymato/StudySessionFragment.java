package it.uni.sim.studymato;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import it.uni.sim.studymato.databinding.FragmentStudySessionBinding;
import it.uni.sim.studymato.model.StudySession;

public class StudySessionFragment extends Fragment {

    FragmentStudySessionBinding binding = null;
    private CountDownTimer studyTimer;
    private long studyInterval;
    private long breakInterval;
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
        binding = FragmentStudySessionBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        toggleBottomNavigationView();
        studyInterval = 5000;
        breakInterval = 5000;
        currentInterval = StudyIntervals.STUDY;
        binding.breakAndResumeButton.setEnabled(false);
        setupTimer(studyInterval);
        studyTimer.start();

        binding.breakAndResumeButton.setOnClickListener(v -> {
            goToNextInterval();
            studyTimer.start();
        });

        binding.endSessionButton.setOnClickListener(v -> {
            //Dialog
            studyTimer.cancel();
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
                closeWindow();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(
                this, callback);
    }

    private void setupTimer(long interval) {
        studyTimer = new CountDownTimer(interval, 1000) {
            @Override
            public void onTick(long l) {
                binding.timerTextView.setText("Time elapsed: " + l / 1000);
            }

            @Override
            public void onFinish() {
                binding.timerTextView.setText("Done");
                binding.breakAndResumeButton.setEnabled(true);
            }
        };
    }

    private void goToNextInterval() {
        binding.breakAndResumeButton.setEnabled(false);
        //TODO: Send notification
        if (currentInterval == StudyIntervals.STUDY) {
            currentInterval = StudyIntervals.BREAK;
            numberOfStudyIntervals += 1;
            setupTimer(breakInterval);
            binding.breakAndResumeButton.setText("Resume");
            binding.progressTextView.setText(numberOfStudyIntervals*(studyInterval / 1000) + " minutes");
        }
        else {
            currentInterval = StudyIntervals.STUDY;
            setupTimer(studyInterval);
            binding.breakAndResumeButton.setText("Take a break");
        }
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