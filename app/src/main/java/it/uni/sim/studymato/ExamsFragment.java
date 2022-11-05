package it.uni.sim.studymato;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import it.uni.sim.studymato.databinding.FragmentExamsBinding;
import it.uni.sim.studymato.model.Exam;

public class ExamsFragment extends Fragment {

    FragmentExamsBinding binding = null;
    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference ref = mDatabase.getReference();
    private RecyclerView rv;
    CustomExamsAdapter adapter;


    public ExamsFragment() {
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
        binding = FragmentExamsBinding.inflate(inflater, container, false);

        rv = binding.rvExams;
        FloatingActionButton fab = binding.fab;
        DatabaseReference examsRef = ref.child("exams");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddExamFragment addExamFragment= new AddExamFragment();
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_nav_host_fragment, addExamFragment, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseRecyclerOptions<Exam> options = new FirebaseRecyclerOptions.Builder<Exam>()
                .setQuery(examsRef, Exam.class)
                .build();

        adapter = new CustomExamsAdapter(options);
        rv.setAdapter(adapter);

        customizeRecyclerView();

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

    private void customizeRecyclerView() {
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv.getContext(),
                LinearLayoutManager.VERTICAL);
        rv.addItemDecoration(dividerItemDecoration);

        new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                int swipeFlags = ItemTouchHelper.LEFT;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAbsoluteAdapterPosition();
                Exam deletedExam = adapter.getItem(position);
                DatabaseReference examsRef = ref.child("exams");
                Query q = examsRef.orderByChild("name").equalTo(deletedExam.getName());
                q.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            DataSnapshot ds = snapshot.getChildren().iterator().next();
                            examsRef.child(Objects.requireNonNull(ds.getKey())).removeValue().addOnCompleteListener(task -> {
                                if(!task.isSuccessful()) {
                                    Log.e("firebase", "Error deleting data", task.getException());
                                }
                                else {
                                    adapter.notifyItemRemoved(position);
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }).attachToRecyclerView(rv);

    }
}