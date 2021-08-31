package com.example.driverapplication.ui.bottomSheet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.driverapplication.DriverClient;
import com.example.driverapplication.Model.CustomService;
import com.example.driverapplication.Model.DriverModel;
import com.example.driverapplication.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Locale;
import java.util.function.UnaryOperator;

public class ShipmentItemFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<CustomService> services=new ArrayList<>();
    private int position;
    public  static onClickConfirmationCode onClickConfirmationCode;

    public static ShipmentItemFragment.onClickConfirmationCode getOnClickConfirmationCode() {
        return onClickConfirmationCode;
    }

    public static void setOnClickConfirmationCode(ShipmentItemFragment.onClickConfirmationCode onClickConfirmationCode1) {
        onClickConfirmationCode = onClickConfirmationCode1;
    }

    public interface onClickConfirmationCode{
        public void onClickConfirmationCode(boolean isValid,int position);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public ArrayList<CustomService> getServices() {
        return services;
    }

    public void setServices(ArrayList<CustomService> services) {
        this.services = services;
    }

    private String mParam1;
    private String mParam2;
    private CustomService service;
    private DriverModel driverModel;

    public CustomService getService() {
        return service;
    }

    public void setService(CustomService service) {
        this.service = service;
    }

    public ShipmentItemFragment() {
        // Required empty public constructor
    }
    public ShipmentItemFragment(CustomService service){
        this.service=service;
    }


    public static ShipmentItemFragment newInstance(CustomService service,int i) {

        ShipmentItemFragment fragment = new ShipmentItemFragment();
        fragment.setService(service);
        fragment.setPosition(i);
        Bundle args = new Bundle();


        fragment.setArguments(args);
        return fragment;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shipment_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MaterialTextView receiverName = view.findViewById(R.id.receiver_name);
        MaterialTextView phoneNumber = view.findViewById(R.id.phone_number);
        AppCompatImageButton callReceiver = view.findViewById(R.id.call_receiver);
        MaterialTextView deliveryAddress = view.findViewById(R.id.delivery_address);
        TextInputLayout codeInput = view.findViewById(R.id.verification_code_text_input);
        MaterialButton verify=view.findViewById(R.id.confirm_code_btn);
        if(((DriverClient)getActivity().getApplicationContext()).getDriver()!=null) {
            driverModel = ((DriverClient) getActivity().getApplicationContext()).getDriver();
        }
        else {
            Task<DataSnapshot> drivers = FirebaseDatabase.getInstance().getReference("Drivers").child(FirebaseAuth.getInstance().getUid()).get();
            drivers.addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    driverModel=dataSnapshot.getValue(DriverModel.class);
                    ((DriverClient) getActivity().getApplicationContext()).setDriver(driverModel);
                }
            });
        }


        receiverName.setText("TO :"+service.getCustomerName());
        phoneNumber.setText("phone number : "+service.getCustomerPhoneNumber());
        callReceiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", service.getCustomerPhoneNumber(), null));
                startActivity(intent);
            }
        });
        deliveryAddress.setText("Address : "+service.getAddress().getName());

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = codeInput.getEditText().getText().toString();
                if(service.getItemDeliveryVerificationCode().equals(s))
                {
                    DocumentReference document = FirebaseFirestore.getInstance().collection("users").document(driverModel.getCurrentJob().getUserID()).collection("jobs").document(driverModel.getCurrentJob().getJobID());
                    services.get(position).setStatus("delivered");
                    services.get(position).setDelivered(true);
                    document.update("services", services);
                    com.example.driverapplication.ui.bottomSheet.ShipmentItemFragment.onClickConfirmationCode.onClickConfirmationCode(true,position);
                }


            }
        });

    }
}