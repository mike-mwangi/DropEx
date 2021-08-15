package com.example.dropex.ui.shipments.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.dropex.FetchGeocodingConfig;
import com.example.dropex.FetchGeocodingTask;
import com.example.dropex.FetchGeocodingTaskCallbackInterface;
import com.example.dropex.FetchSolutionConfig;
import com.example.dropex.FetchSolutionTask;
import com.example.dropex.Model.CustomService;
import com.example.dropex.Model.CustomVehicle;
import com.example.dropex.Model.ItemSize;
import com.example.dropex.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.graphhopper.directions.api.client.model.Address;
import com.graphhopper.directions.api.client.model.GeocodingLocation;
import com.graphhopper.directions.api.client.model.Service;
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


public class ShippingItemFragment extends Fragment implements FetchGeocodingTaskCallbackInterface, AutoCompleteAdapter.OnItemClickListener{

    private static final String FRAGMENT_NUMBER = "param1";


    private TextInputEditText deliveryAddress;
    private TextInputEditText itemNote;

    private EditText locationInput;
    private ArrayList<GeocodingLocation> locations;
    private GeocodingLocation deliveryLocation;
    private GeocodingLocation pickuplocation;
    private TextInputEditText customerNameTextInput;
    private ChipGroup sizeChips;
    private TextInputEditText customerPhoneNumberInput;
    public String sizeOfItemString;

//if item number is negative then this means that we are at the final page an trying to finish
    private int itemNumber;
    private AutoCompleteAdapter autoCompleteAdapter1;


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
        itemNote=view.findViewById(R.id.item_note_input);
        sizeChips=view.findViewById(R.id.size_input_chips);
       locationInput=view.findViewById(R.id.location_input);
       customerNameTextInput=view.findViewById(R.id.name_of_recipient);
       customerPhoneNumberInput=view.findViewById(R.id.receiver_phone_number_input);

       sizeChips.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               final int checkedChipId = sizeChips.getCheckedChipId();
               final Chip checkedChip = view.findViewById(checkedChipId);
               sizeOfItemString=checkedChip.getText().toString();
           }
       });


        locations= new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.location_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        autoCompleteAdapter1= new AutoCompleteAdapter();
        recyclerView.setAdapter(autoCompleteAdapter1);

      autoCompleteAdapter1.setOnItemClickListener(this);

        ImageButton searchButton=view.findViewById(R.id.search_btn);
locationInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){
            recyclerView.setVisibility(View.VISIBLE);
        }
        else{
            recyclerView.setVisibility(View.INVISIBLE);
        }
    }
});
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address=locationInput.getText().toString();
                new FetchGeocodingTask(ShippingItemFragment.this,getString(R.string.gh_key)).execute(new FetchGeocodingConfig(address, "en", 5, false, "-1.2778285,36.8486", "default"));
            }
        });


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

    public CustomService getService() {
        String customerName=customerNameTextInput.getText().toString();
        String deliveryNote=itemNote.getText().toString();
        String customerPhoneNumber=customerPhoneNumberInput.getText().toString();
        int sizeOfItem= ItemSize.getSizeOfItem(sizeOfItemString);

        CustomService service=new CustomService();
        service.setCustomerName(customerName);
        service.setDeliveryNote(deliveryNote);
        service.setCustomerPhoneNumber(customerPhoneNumber);
        service.setCustomSize(sizeOfItem);

        Stop stop=new Stop();
        Address address=new Address();
        address.setLat(deliveryLocation.getPoint().getLat());
        address.setLon(deliveryLocation.getPoint().getLng());
        address.setName(deliveryLocation.getName());
        stop.setAddress(address);
        service.setAddress(address);

        String shimentId=customerName+customerPhoneNumber;
        service.setId(shimentId);
        service.setName("pickup and deliver to me");
        long maxT=9222036;
        service.setMaxTimeInVehicle(maxT);

        if(service.getCustomerName()==""){
            service=null;
        }
        else if(service.getCustomerPhoneNumber()=="")
        {
            service=null;
        }
        else if(service.getDeliveryNote()=="")
        {
            service=null;
        } else if(deliveryLocation==null)
        {
            service=null;
        }


        return service;
    }

    @Override
    public void onError(int message) {

    }

    @Override
    public void onPostExecuteGeocodingSearch(List<GeocodingLocation> points) {



 autoCompleteAdapter1.setLocations((ArrayList<GeocodingLocation>) points);

 //deliveryLocation=points.get(0);
 //pickuplocation=points.get(2);
       // Log.e("GEOCODE",points.toString());
    }

    @Override
    public void onItemClick(GeocodingLocation location) {
        deliveryLocation=location;
        locationInput.setText(location.getCountry()+","+location.getCity()+","+location.getName());
    }

}