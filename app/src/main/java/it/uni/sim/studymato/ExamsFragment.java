package it.uni.sim.studymato;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import it.uni.sim.studymato.databinding.FragmentExamsBinding;
import it.uni.sim.studymato.model.Exam;

public class ExamsFragment extends Fragment {

    FragmentExamsBinding binding = null;
    private DatabaseReference mDatabase;
    CustomExamsAdapter adapter;


    public ExamsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseRecyclerOptions<Exam> options
                = new FirebaseRecyclerOptions.Builder<Exam>()
                .setQuery(mDatabase, Exam.class)
                .build();

        //adapter = new CustomExamsAdapter(options);
        binding.rvExams.setAdapter(adapter);
        binding.rvExams.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentExamsBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}