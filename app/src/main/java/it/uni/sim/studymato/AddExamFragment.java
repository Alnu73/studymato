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
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import it.uni.sim.studymato.databinding.FragmentAddExamBinding;
import it.uni.sim.studymato.model.Exam;

public class AddExamFragment extends Fragment {

    FragmentAddExamBinding binding = null;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();


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

        final MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Pick a date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        binding.dueDateTextInputLayout.setEndIconOnClickListener(v -> {
            datePicker.show(requireActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
        });

        datePicker.addOnPositiveButtonClickListener(v -> {
            binding.dueDateEditText.setText(datePicker.getHeaderText());
        });

        binding.confirmButton.setOnClickListener(v -> {
            DatabaseReference examsRef = ref.child("exams");
            DatabaseReference examsRef2 = examsRef.push();
            examsRef2.setValue(new Exam(binding.examNameEditText.getText().toString(),
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