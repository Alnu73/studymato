package it.uni.sim.studymato;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;
import java.util.Date;

import it.uni.sim.studymato.model.Exam;

public class CustomExamsAdapter extends FirebaseRecyclerAdapter<Exam, CustomExamsAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView examTextView;
        private final TextView creditsTextView;
        private final TextView dueDateTextView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            examTextView = view.findViewById(R.id.examTextView);
            creditsTextView = view.findViewById(R.id.creditsTextView);
            dueDateTextView = view.findViewById(R.id.dueDateTextView);

            view.setOnClickListener(v -> mClickListener.onItemClick(v, getAbsoluteAdapterPosition()));
        }

        public TextView getExamTextView() {
            return examTextView;
        }

        private ViewHolder.ClickListener mClickListener;

        public ClickListener getmClickListener() {
            return mClickListener;
        }

        public interface ClickListener {
            void onItemClick(View view, int position);
        }

        public void setOnClickListener(ViewHolder.ClickListener clickListener){
            mClickListener = clickListener;
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
        holder.creditsTextView.setText(String.valueOf(model.getCredits()));
        Date date = new Date(model.getDueDate());
        holder.dueDateTextView.setText(String.valueOf(date).substring(0, 10));

        holder.setOnClickListener((view, pos) -> {
            //Open new fragment
            //TODO: Da rivedere!
            ExamStatsFragment examStatsFragment = new ExamStatsFragment();
            Bundle args = new Bundle();
            args.putString("examName",model.getName());
            args.putInt("examCredits", model.getCredits());
            args.putLong("examCredits", model.getDueDate());
            examStatsFragment.setArguments(args);
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_nav_host_fragment, examStatsFragment, "studySessionFragment")
                    .addToBackStack(null)
                    .commit();
        });
    }
}
