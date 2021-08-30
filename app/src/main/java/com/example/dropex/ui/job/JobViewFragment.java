package com.example.dropex.ui.job;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dropex.Adapter.ShipmentItemAdapter;
import com.example.dropex.FetchSolutionCallBackInterfaceButWithJobSolution;
import com.example.dropex.FetchSolutionConfig;
import com.example.dropex.FetchSolutionTaskButReturnJobSolution;
import com.example.dropex.Model.CustomService;
import com.example.dropex.Model.CustomerModel;
import com.example.dropex.Model.Job;
import com.example.dropex.Model.JobSolution;
import com.example.dropex.NavigationLauncherActivity;
import com.example.dropex.R;
import com.example.dropex.UserClient;
import com.example.dropex.ui.shipments.main.ShippingInformationFragment;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.graphhopper.directions.api.client.model.Service;
import com.graphhopper.directions.api.client.model.Solution;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Callback;


public class JobViewFragment extends Fragment implements ShipmentItemAdapter.shipmentItemOnClickListener , FetchSolutionCallBackInterfaceButWithJobSolution {
    private static final String JOB_ID = "JOB_ID";
    private String jobID;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference jobRef ;
    CustomerModel customer;
    Job job;
    ShipmentItemAdapter adapter;
    RecyclerView recyclerView;
    private MaterialTextView cost_type;
    private MaterialTextView cost;
    private TextInputEditText pickUpLocation;
    private ExtendedFloatingActionButton saveEdit;




    public JobViewFragment() {
        // Required empty public constructor
    }

    public static JobViewFragment newInstance(String jobID) {
        JobViewFragment fragment = new JobViewFragment();
        Bundle args = new Bundle();
        args.putString(JOB_ID, jobID);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            jobID= getArguments().getString(JOB_ID);
        }
        customer=((UserClient)getActivity().getApplicationContext()).getCustomer();
        job=customer.getCurrentJob();
      //  jobRef=db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("jobs").document(job.getJobID()).get("services");

    }
    private void setUpRecyclerView() {
        adapter = new ShipmentItemAdapter();
        adapter.setServices(job.getServices());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setOnClickListener(this);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cost_type=view.findViewById(R.id.cost_type);
        cost=view.findViewById(R.id.cost);
        pickUpLocation= (TextInputEditText) ((TextInputLayout)view.findViewById(R.id.pick_location_layout)).getEditText();
        pickUpLocation.setText(job.getPickUpLocation().getName());
        saveEdit=view.findViewById(R.id.saveEdit);
        saveEdit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent=new Intent(getContext(),NavigationLauncherActivity.class);
                        intent.putExtra("JOBID",job.getJobID());
                        startActivity(intent);

                    }
                }
        );
           cost.setText(String.valueOf(job.getSolution().getCosts().intValue()));


        recyclerView=view.findViewById(R.id.shipment_items_recyclerview);
        setUpRecyclerView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_job_view, container, false);
    }


    @Override
    public void itemOnClickListener(String action, List<CustomService> services) {

            job.setServices((ArrayList<CustomService>) services);
            saveJob();

    }
    public void saveJob(){
        final String s = job.buildJsonRequest();
        //to go back to previous version just remove the .enque from here to job posttoGraphopper
        job.postToGraphhopper(s, this.getActivity()).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.e("failure","dont know what happened");
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                String jsonData = response.body().string();
                JSONObject Jobject = null;
                try {
                    Jobject = new JSONObject(jsonData);
                    String jobID= (String) Jobject.get("job_id");



                    Log.e("JOB ID",jobID);
                    String previousJobID=job.getJobID();
                    job.setJobID(jobID);
                    FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("jobs").document(previousJobID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("jobs").document(jobID).set(job).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {
                                    Log.e("Shippin",e.toString());
                                }
                            })
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            new FetchSolutionTaskButReturnJobSolution(JobViewFragment.this, getString(R.string.gh_key)).execute(new FetchSolutionConfig(job.getJobID(), "default"));
                                        }
                                    });
                        }
                    });
            /*        Job job=currentCustomer.getCurrentJob();
                    // new FetchSolutionTaskButReturnJobSolution(ShippingInformationFragment.this,getString(R.string.gh_key)).execute(new FetchSolutionConfig(job.getJobID(), "default"));


                    Intent goToNavLauncher=new Intent(ShippingInformationFragment.this.getActivity(), NavigationLauncherActivity.class);
                    Uri uri=Uri.parse("https://graphhopper.com/api/1/vrp/solution/"+currentCustomer.getCurrentJob().getJobID()+"?vehicle_id="+"default"+"&key="+getString(R.string.gh_key));
                    goToNavLauncher.setData(uri);
                   FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("jobs").document(currentCustomer.getCurrentJob().getJobID()).set(currentCustomer.getCurrentJob()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            new FetchSolutionTaskButReturnJobSolution(ShippingInformationFragment.this, getString(R.string.gh_key)).execute(new FetchSolutionConfig(job.getJobID(), "default"));
                        }
                    });*/



                    // startActivity(goToNavLauncher);






                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    @Override
    public void onError(int message) {

    }

    @Override
    public void onPostExecute(Solution jobSolution) {
        Map<String,Object> solution=new HashMap<>();
        solution.put("solution",jobSolution);
        final Task<Void> update = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("jobs").document(job.getJobID()).update(solution);
        update.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Intent goToNavLauncher=new Intent(JobViewFragment.this.getActivity(), NavigationLauncherActivity.class);
                goToNavLauncher.putExtra("JOBID",job.getJobID());
                startActivity(goToNavLauncher);

            }
        });
        update.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.e("JOBVIEWFRAGMENT",e.toString());
            }
        });

    }
}