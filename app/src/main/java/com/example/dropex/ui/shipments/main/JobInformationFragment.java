package com.example.dropex.ui.shipments.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.dropex.FetchGeocodingConfig;
import com.example.dropex.FetchGeocodingTask;
import com.example.dropex.FetchGeocodingTaskCallbackInterface;
import com.example.dropex.Model.CustomerModel;
import com.example.dropex.Model.Job;
import com.example.dropex.R;
import com.example.dropex.UserClient;
import com.firebase.ui.auth.data.model.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.graphhopper.directions.api.client.model.GeocodingLocation;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;



public class JobInformationFragment extends Fragment implements FetchGeocodingTaskCallbackInterface,AutoCompleteAdapter.OnItemClickListener{

    private TextInputEditText amount;
    private TextInputEditText locationInput;
    private ArrayList<GeocodingLocation> locations;
    private AutoCompleteAdapter autoCompleteAdapter1;
    private MaterialButton next;
    private GeocodingLocation pickupLocation;
    private CustomerModel currentCustomer;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public static JobInformationFragment newInstance(String param1, String param2) {
        JobInformationFragment fragment = new JobInformationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public JobInformationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(((UserClient)getActivity().getApplicationContext()).getCustomer() != null) {
            currentCustomer = ((UserClient) getActivity().getApplicationContext()).getCustomer();
        }
        else {
           FirebaseDatabase.getInstance().getReference("Customers").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    currentCustomer =snapshot.getValue(CustomerModel.class);
                    ((UserClient) getActivity().getApplicationContext()).setCustomer(currentCustomer);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_job_information, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        amount=view.findViewById(R.id.package_quantity);

        next=view.findViewById(R.id.btn_job_info);

        locationInput=view.findViewById(R.id.location_input);

        locations= new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.location_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        autoCompleteAdapter1= new AutoCompleteAdapter();
        recyclerView.setAdapter(autoCompleteAdapter1);

        autoCompleteAdapter1.setOnItemClickListener(this);

        ImageButton searchButton=view.findViewById(R.id.search_btn);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address=locationInput.getText().toString();
                new FetchGeocodingTask(JobInformationFragment.this,getString(R.string.gh_key)).execute(new FetchGeocodingConfig(address, "en", 5, false, "-1.2778285,36.8486", "default"));
            }
        });



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(amount.getText().toString().length()>0 && pickupLocation!=null) {
                    JobInformationFragmentDirections.ActionJobInformationToShippingInformation action =
                            JobInformationFragmentDirections.actionJobInformationToShippingInformation();
                    int quantity = Integer.parseInt(amount.getText().toString());
                    action.setNumberOfShipments(quantity);
                    currentCustomer.setCurrentJob(new Job());
                    currentCustomer.getCurrentJob().setPickUpLocation(pickupLocation);
                    ((UserClient) getActivity().getApplicationContext()).setCustomer(currentCustomer);
                    Navigation.findNavController(view).navigate(action);
                }
                else {
                    if(pickupLocation==null) {
                        locationInput.setError("please choose a valid pick up location");
                    }
                    if(amount.getText().toString().length()<1){


                            amount.setError("at least one package needs to be delivered");

                    }
                }
            }

        });

    }

    @Override
    public void onError(int message) {

    }

    @Override
    public void onPostExecuteGeocodingSearch(List<GeocodingLocation> points) {
        autoCompleteAdapter1.setLocations((ArrayList<GeocodingLocation>) points);

    }

    @Override
    public void onItemClick(GeocodingLocation location) {
        pickupLocation=location;
        locationInput.setText(location.getCountry()+","+location.getCity()+","+location.getName());
    }
}