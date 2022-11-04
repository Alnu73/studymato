package it.uni.sim.studymato;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;

import it.uni.sim.studymato.model.Exam;

public class CustomExamsAdapter extends FirebaseRecyclerAdapter<Exam, CustomExamsAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView examTextView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            examTextView = view.findViewById(R.id.examTextView);
        }

        public TextView getExamTextView() {
            return examTextView;
        }
    }

    public CustomExamsAdapter(FirebaseRecyclerOptions<Exam> options) {
        super(options);
    }

    @NonNull
    @Override
    public CustomExamsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_row_item, parent, false);
        return new CustomExamsAdapter.ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Exam model) {
        holder.examTextView.setText(model.getName());
    }
}
