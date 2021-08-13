package com.example.dropex.ui.job;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import com.example.dropex.Model.Job;
import com.example.dropex.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class JobsRecyclerViewAdapter extends FirestoreRecyclerAdapter<Job,JobsRecyclerViewAdapter.JobViewHolder> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public JobsRecyclerViewAdapter(@NonNull @NotNull FirestoreRecyclerOptions<Job> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull JobsRecyclerViewAdapter.JobViewHolder holder, int position, @NonNull @NotNull Job model) {
        holder.jobTitle.setText(model.getJobID());
        holder.timestamp.setText("");

    }

    @NonNull
    @NotNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_item,
                parent, false);
        return new JobViewHolder(v);
    }

    public class JobViewHolder extends RecyclerView.ViewHolder{
public TextView jobTitle;
public TextView timestamp;
        public JobViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            jobTitle=itemView.findViewById(R.id.job_title);
            timestamp=itemView.findViewById(R.id.timestamp);
        }
    }
}
