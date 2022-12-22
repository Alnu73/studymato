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
import it.uni.sim.studymato.databinding.FragmentExamStatsBinding;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Date;

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
        Bundle args = this.getArguments();
        Date dueDate = new Date(args.getLong("examDueDate"));
        String descriptionText = args.getInt("examCredits") + " credits, for " + String.valueOf(dueDate).substring(0, 10);
        binding.examNameTextView.setText(args.getString("examName"));
        binding.examDescriptionTextView.setText(descriptionText);
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<String>();
        entries.add(new BarEntry(20f, 3));
        entries.add(new BarEntry(15f, 4));
        entries.add(new BarEntry(19f, 5));
        BarChart barChart = binding.barchart;



        BarDataSet bardataset = new BarDataSet(entries, "Cells");
        BarData data = new BarData(bardataset);
        barChart.setData(data); // set the data and list of labels into chart
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        barChart.animateY(2000);
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