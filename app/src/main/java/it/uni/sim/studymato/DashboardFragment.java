package it.uni.sim.studymato;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import it.uni.sim.studymato.databinding.FragmentDashboardBinding;
import it.uni.sim.studymato.model.Exam;

public class DashboardFragment extends Fragment {

    FragmentDashboardBinding binding = null;
    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference ref = mDatabase.getReference();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public DashboardFragment() {
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
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        DatabaseReference examsRef = ref.child("exams");
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            binding.welcomeTextView.append(currentUser.getDisplayName());
        }
        Query q = examsRef.orderByChild("dueDate").limitToFirst(1);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DataSnapshot ds = snapshot.getChildren().iterator().next();
                    Exam exam = ds.getValue(Exam.class);
                    if (exam != null) {
                        Long diff = exam.getDueDate() - System.currentTimeMillis();
                        Long daysDiff = TimeUnit.MILLISECONDS.toDays(diff);
                        binding.nextDeadlineTextView.append(exam.getName() + " in " + daysDiff + " days");
                    }
                }
                else {
                    binding.nextDeadlineTextView.setText("None");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}