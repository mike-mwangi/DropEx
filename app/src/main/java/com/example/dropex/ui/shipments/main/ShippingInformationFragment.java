package com.example.dropex.ui.shipments.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.dropex.FetchSolutionCallBackInterfaceButWithJobSolution;
import com.example.dropex.FetchSolutionTaskButReturnJobSolution;
import com.example.dropex.FetchSolutionTaskCallbackInterface;
import com.example.dropex.Model.CustomService;
import com.example.dropex.Model.Job;
import com.example.dropex.Model.JobSolution;
import com.example.dropex.NavigationLauncherActivity;
import com.example.dropex.R;

import com.example.dropex.ui.main.OnboardingFragment;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.graphhopper.directions.api.client.model.Service;
import com.graphhopper.directions.api.client.model.Shipment;
import com.mapbox.geojson.Point;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Callback;

import static com.example.dropex.Common.Common.currentCustomer;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShippingInformationFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ShippingInformationFragment extends Fragment implements View.OnClickListener , FetchSolutionCallBackInterfaceButWithJobSolution {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static int currentPage = 0;
    private static ArrayList<ShippingItemFragment> fragments = new ArrayList<ShippingItemFragment>();
    private ViewPager2 viewPager;
    private OnBoardingViewPagerAdapter adapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public static ShippingInformationFragment newInstance(String param1, String param2) {
        ShippingInformationFragment fragment = new ShippingInformationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ShippingInformationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final int numberOfShipments = ShippingInformationFragmentArgs.fromBundle(getArguments()).getNumberOfShipments();
        viewPager=(ViewPager2) view.findViewById(R.id.viewpager);
        TabLayout tabLayout=view.findViewById(R.id.tab);
        final Button startButton=view.findViewById(R.id.next_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


        adapter = new OnBoardingViewPagerAdapter(getFragmentManager(),this.getLifecycle());
        for (int i=1;i<=numberOfShipments;i++) {
            if(i<numberOfShipments) {

                fragments.add(ShippingItemFragment.newInstance(i));

            }else {
                fragments.add(ShippingItemFragment.newInstance(-1));
                startButton.setText("FINISH");
                startButton.setOnClickListener(this);

            }
        }
        adapter.setFragments(fragments);
        viewPager.setAdapter(adapter);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPage=position;
            }
        });
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText("")
        ).attach();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_shipping_information, container, false);

        return root;
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        ArrayList<CustomService> services=new ArrayList<>();
        switch (id){
            case R.id.next_button:
                ArrayList<Integer> flagIncompleteForms=new ArrayList<>();

                for(int i=0;i<fragments.size();i++){
                    ShippingItemFragment fragment=fragments.get(i);
                    if(fragment.getService()==null){
                        flagIncompleteForms.add(i);
                    }
                    else {
                        services.add(fragment.getService());
                    }
                }

                    currentCustomer.getCurrentJob().setServices(services);
                    Gson gson = new Gson();
                    final String s = currentCustomer.getCurrentJob().buildJsonRequest();
                    //to go back to previous version just remove the .enque from here to job posttoGraphopper
                    currentCustomer.getCurrentJob().postToGraphhopper(s, this.getActivity()).enqueue(new Callback() {
                        @Override
                        public void onFailure(okhttp3.Call call, IOException e) {
                            Log.e("failure","dont know what happened");
                        }

                        @Override
                        public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                            String jsonData = response.body().string();
                            JSONObject Jobject = null;
                            try {
                                JobSolution jobSolution=new JobSolution();
                                Jobject = new JSONObject(jsonData);
                                String jobID= (String) Jobject.get("job_id");



                                Log.e("JOB ID",jobID);
                                currentCustomer.getCurrentJob().setJobID(jobID);
                                currentCustomer.getCurrentJob().setSolution(jobSolution);
                                FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("jobs").document(currentCustomer.getCurrentJob().getJobID()).set(currentCustomer.getCurrentJob());
                                new FetchSolutionTaskButReturnJobSolution(ShippingInformationFragment.this,getString(R.string.gh_key)).execute();
                                /*

                                Intent goToNavLauncher=new Intent(activity, NavigationLauncherActivity.class);
                                String uriString="https://graphhopper.com/api/1/vrp/solution/"+currentCustomer.getCurrentJob().getJobID()+"?vehicle_id="+"default"+"&key="+getString(R.string.gh_key);
                                Uri uri=Uri.parse("https://graphhopper.com/api/1/vrp/solution/"+currentCustomer.getCurrentJob().getJobID()+"?vehicle_id="+"default"+"&key="+activity.getString(R.string.gh_key));
                                goToNavLauncher.setData(uri);
                                FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("jobs").document(currentCustomer.getCurrentJob().getJobID()).set(currentCustomer.getCurrentJob());
                                Log.e("URI", String.valueOf(uri));
                                Log.e("URI string", uriString);

                                activity.startActivity(goToNavLauncher);

                                 */




                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });;
                    currentCustomer.getJobs().add(currentCustomer.getCurrentJob());





                break;

        }
    }

    @Override
    public void onError(int message) {

    }

    @Override
    public void onPostExecute(JobSolution jobSolution) {
        Map<String,Object> solution=new HashMap<>();
        solution.put("jobSolution",jobSolution);
        FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("jobs").document(currentCustomer.getCurrentJob().getJobID()).update(solution);
        ShippingInformationFragmentDirections.ActionShippingInformationToMeansOfTransport action=ShippingInformationFragmentDirections.actionShippingInformationToMeansOfTransport();
        int costs= jobSolution.getCosts();
        action.setCost(costs);
        Navigation.findNavController(ShippingInformationFragment.this.getView()).navigate(action);


    }
}