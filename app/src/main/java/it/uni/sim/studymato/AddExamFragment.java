package it.uni.sim.studymato;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.uni.sim.studymato.databinding.FragmentAddExamBinding;
import it.uni.sim.studymato.model.Exam;

public class AddExamFragment extends Fragment {

    FragmentAddExamBinding binding = null;
    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference ref = mDatabase.getReference();

    final MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Pick a date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build();

    public AddExamFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddExamBinding.inflate(inflater, container, false);
        toggleBottomNavigationView();

        binding.dueDateTextInputLayout.setEndIconOnClickListener(v -> {
            datePicker.show(requireActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
        });

        datePicker.addOnPositiveButtonClickListener(v -> {
            binding.dueDateEditText.setText(datePicker.getHeaderText());
        });

        binding.confirmButton.setOnClickListener(v -> {
            if(!checkFieldsCorrect()) {
                return;
            }
            DatabaseReference examsRef = ref.child("exams");
            DatabaseReference ref = examsRef.push();
            ref.setValue(new Exam(binding.examNameEditText.getText().toString(),
                    Integer.parseInt(binding.numberOfCreditsEditText.getText().toString()),
                    datePicker.getSelection()));
            closeWindow();
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

    private void closeWindow() {
        NavHostFragment navHostFragment =
                (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.main_nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        NavDirections action = AddExamFragmentDirections.actionAddExamFragmentToExamsFragment();
        navController.navigate(action);
        toggleBottomNavigationView();
    }

    private boolean checkFieldsCorrect() {
        if (binding.examNameEditText.getText().toString().matches("")) {
            Toast.makeText(getContext(), "You must enter a valid exam name!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.numberOfCreditsEditText.getText().toString().matches("")) {
            Toast.makeText(getContext(), "You must enter a valid number of credits!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.dueDateEditText.getText().toString().matches("")) {
            Toast.makeText(getContext(), "You must enter a valid number of credits!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (datePicker.getSelection() == null || datePicker.getSelection() < System.currentTimeMillis()) {
            Toast.makeText(getContext(), "You must enter a date in the future!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void toggleBottomNavigationView() {
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView);
        if(bottomNavigationView.getVisibility() == View.VISIBLE)
            bottomNavigationView.setVisibility(View.GONE);
        else
            bottomNavigationView.setVisibility(View.VISIBLE);
    }
}