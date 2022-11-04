package it.uni.sim.studymato;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import it.uni.sim.studymato.authentication.SignInFragmentDirections;
import it.uni.sim.studymato.databinding.FragmentExamsBinding;
import it.uni.sim.studymato.model.Exam;

public class ExamsFragment extends Fragment {

    FragmentExamsBinding binding = null;
    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference ref = mDatabase.getReference();
    CustomExamsAdapter adapter;


    public ExamsFragment() {
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
        binding = FragmentExamsBinding.inflate(inflater, container, false);

        FloatingActionButton fab = binding.fab;
        RecyclerView rv = binding.rvExams;
        DatabaseReference examsRef = ref.child("exams");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment navHostFragment =
                        (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.main_nav_host_fragment);
                NavController navController = navHostFragment.getNavController();
                NavDirections action = ExamsFragmentDirections.actionExamsFragmentToAddExamFragment();
                navController.navigate(action);
            }
        });

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseRecyclerOptions<Exam> options = new FirebaseRecyclerOptions.Builder<Exam>()
                .setQuery(examsRef, Exam.class)
                .build();

        adapter = new CustomExamsAdapter(options);
        rv.setAdapter(adapter);


        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.stopListening();
        binding = null;
    }
}