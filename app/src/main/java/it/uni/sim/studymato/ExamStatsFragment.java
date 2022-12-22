package it.uni.sim.studymato;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import it.uni.sim.studymato.databinding.FragmentDashboardBinding;
import it.uni.sim.studymato.databinding.FragmentExamStatsBinding;

public class ExamStatsFragment extends Fragment {

    FragmentExamStatsBinding binding = null;

    public ExamStatsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentExamStatsBinding.inflate(inflater, container, false);
        toggleBottomNavigationView();
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