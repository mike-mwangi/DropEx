package com.example.dropex.ui.job;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import com.example.dropex.Model.CustomerModel;
import com.example.dropex.Model.Job;
import com.example.dropex.R;
import com.example.dropex.UserClient;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.textview.MaterialTextView;

public class JobsRecyclerViewAdapter extends FirestoreRecyclerAdapter<Job,JobsRecyclerViewAdapter.JobViewHolder> {

    JobOnClickListener onClickListener;

    public void setOnClickListener(JobOnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public interface JobOnClickListener{
        public void onClickJob();
    }

    public JobsRecyclerViewAdapter(@NonNull @NotNull FirestoreRecyclerOptions<Job> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull JobsRecyclerViewAdapter.JobViewHolder holder, int position, @NonNull @NotNull Job model) {
        holder.jobTitle.setText(model.getPickUpLocation().getName());
        if(model.getTime()!=null) {
            holder.timestamp.setText(model.getTime().toString());
        }
        else {
            holder.timestamp.setText("unspecified time");
        }
        holder.noPackages.setText("No. of Packages :"+model.getServices().size());

        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ( (UserClient) v.getContext().getApplicationContext()).getCustomer().setCurrentJob(model);
                onClickListener.onClickJob();

            }
        });


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
public MaterialTextView jobTitle;
public MaterialTextView timestamp;
public MaterialTextView noPackages;
public View mainView;
        public JobViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            jobTitle=itemView.findViewById(R.id.job_title);
            timestamp=itemView.findViewById(R.id.timestamp);
            noPackages=itemView.findViewById(R.id.number_of_items);
            mainView= itemView;

        }
    }
}
