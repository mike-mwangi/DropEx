package com.example.dropex.ui.shipments.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.dropex.FetchGeocodingConfig;
import com.example.dropex.FetchGeocodingTask;
import com.example.dropex.FetchGeocodingTaskCallbackInterface;
import com.example.dropex.FetchSolutionConfig;
import com.example.dropex.FetchSolutionTask;
import com.example.dropex.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.graphhopper.directions.api.client.model.Address;
import com.graphhopper.directions.api.client.model.GeocodingLocation;
import com.graphhopper.directions.api.client.model.Shipment;
import com.graphhopper.directions.api.client.model.Stop;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;


import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import timber.log.Timber;

import static com.example.dropex.Common.Common.currentCustomer;


public class ShippingItemFragment extends Fragment implements FetchGeocodingTaskCallbackInterface {

    private static final String FRAGMENT_NUMBER = "param1";


    private TextInputEditText deliveryAddress;
    private TextInputEditText itemNote;
    private MaterialButton button;
    private AutoCompleteTextView autoCompleteTextView;
    private AutoCompleteAdapter autoCompleteAdapter;
    private ArrayList<GeocodingLocation> locations;

    private int itemNumber;




    public static ShippingItemFragment newInstance(int itemNumber) {
        ShippingItemFragment fragment = new ShippingItemFragment();
        Bundle args = new Bundle();
        args.putInt(FRAGMENT_NUMBER,itemNumber );
        fragment.setArguments(args);
        return fragment;
    }

    public ShippingItemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            itemNumber = getArguments().getInt(FRAGMENT_NUMBER);
        }
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        deliveryAddress=view.findViewById(R.id.delivery_address_input);
        itemNote=view.findViewById(R.id.item_note_input);
        button=view.findViewById(R.id.btn_next_item);
        autoCompleteTextView=view.findViewById(R.id.txtAutoSearch);
        locations= new ArrayList<>();
        autoCompleteAdapter = new AutoCompleteAdapter(getContext(), R.layout.location_list_item,locations);
        autoCompleteAdapter.setNotifyOnChange(true);
      autoCompleteTextView.setAdapter(autoCompleteAdapter);
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {

            private boolean shouldAutoComplete = false;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>4) {
                    shouldAutoComplete = true;
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (shouldAutoComplete) {
                    new FetchGeocodingTask(ShippingItemFragment.this,getString(R.string.gh_key)).execute(new FetchGeocodingConfig(s.toString(), "en", 5, false, "-1.2778285,36.8486", "default"));
                }
            }

        });



        if(itemNumber==-1) {
            button.setText("FINISH");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shipping_item, container, false);
    }
    private void fetchVrpSolution(String jobId, String vehicleId) {
       // currentJobId = jobId;
        //currentVehicleId = vehicleId;

        //   showLoading();
        new FetchSolutionTask(null, getString(R.string.gh_key)).execute(new FetchSolutionConfig(jobId, vehicleId));
    }

    public Shipment getShipment() {
        Shipment shipment=new Shipment();
        Stop stop=new Stop();
        Address address=new Address();

        return shipment;
    }

    @Override
    public void onError(int message) {

    }

    @Override
    public void onPostExecuteGeocodingSearch(List<GeocodingLocation> points) {



 autoCompleteAdapter.setLocations((ArrayList<GeocodingLocation>) points);
 

        Log.e("GEOCODE",points.toString());
    }
}