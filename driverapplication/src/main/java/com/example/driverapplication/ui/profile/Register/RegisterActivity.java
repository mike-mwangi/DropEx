package com.example.driverapplication.ui.profile.Register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.driverapplication.Model.ConstantVehicleTypes;
import com.example.driverapplication.Model.CustomVehicle;
import com.example.driverapplication.Model.CustomVehicleType;
import com.example.driverapplication.Model.DriverModel;

import com.example.driverapplication.R;
import com.example.driverapplication.ui.profile.Login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.graphhopper.directions.api.client.model.VehicleType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView register, login_textView;
    private EditText editFirstName, editLastName, editPhoneNumber, editEmail, editCity, editPassword;
    private TextInputLayout vehicleType,manufacturedYear,carPlateNumber;
    ProgressBar progressBar;
    private ArrayList<VehicleType> vehicleTypes=new ArrayList<>();
    private HashMap<String,VehicleType> typeHashMap=new HashMap<>();
    AutoCompleteTextView autoCompleteVehicleType;
    AutoCompleteTextView autoCompleteManufacturedYear;
    VehicleType type;
    int year=0;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        register = (Button) findViewById(R.id.btn_register);
        register.setOnClickListener(this);
        //login_textView = (TextView) findViewById(R.id.login_textView);
//        login_textView.setOnClickListener(this);

        editFirstName = (EditText) findViewById(R.id.edit_first_name);
        editLastName = (EditText) findViewById(R.id.edit_last_name);
        editPhoneNumber = (EditText) findViewById(R.id.edit_phone_number);
        editEmail = (EditText) findViewById(R.id.edit_email);
        editCity = (EditText) findViewById(R.id.edit_city);
        editPassword = (EditText) findViewById(R.id.edit_password);
       // login_textView = (TextView) findViewById(R.id.login_textView);
        vehicleType=(TextInputLayout) findViewById(R.id.vehicle_type);
        manufacturedYear=(TextInputLayout) findViewById(R.id.manufactured_year);
        carPlateNumber=(TextInputLayout) findViewById(R.id.car_plate_number);
        autoCompleteVehicleType=findViewById(R.id.autoComplete_vehicle_type);
        autoCompleteManufacturedYear=findViewById(R.id.autoComplete_manufactured_year);


        vehicleTypes.add(ConstantVehicleTypes.getBikeVehicleType());
        vehicleTypes.add(ConstantVehicleTypes.getCarVehicleType());
        vehicleTypes.add(ConstantVehicleTypes.getVanVehicleType());

        ArrayList<String> typeNames=new ArrayList<>();
        typeNames.addAll(Arrays.asList(new String[]{"Bike", "Car", "Van"}));
        ArrayAdapter<String> vehicleTypeArrayAdapter=new ArrayAdapter<String>(this,R.layout.spinner_list_item,typeNames);
        autoCompleteVehicleType.setAdapter(vehicleTypeArrayAdapter);
        vehicleTypeArrayAdapter.notifyDataSetChanged();
        autoCompleteVehicleType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                type=vehicleTypes.get(position);
        }
        });
        ArrayList<String> yearList=new ArrayList<>();
        yearList.addAll(Arrays.asList(new String[]{"2002","2003","2004","2005","2006","2007","2008","2009","2010","2011","2012","2013",}));
        ArrayAdapter<String> manufacturedYearAdapter=new ArrayAdapter<String>(this,R.layout.spinner_list_item,yearList);
        autoCompleteManufacturedYear.setAdapter(manufacturedYearAdapter);
        autoCompleteManufacturedYear.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                year=Integer.parseInt(yearList.get(position));
            }
        });
        progressBar = (ProgressBar) findViewById(R.id.progressBar);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        /*    case R.id.login_textView:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;

         */
            case R.id.btn_register:
                registerUser();
//                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                break;
        }

    }

    private void registerUser() {
        String firstName = editFirstName.getText().toString().trim();
        String lastName = editLastName.getText().toString().trim();
        String phone_number = editPhoneNumber.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String city = editCity.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String sCarPlateNumber=carPlateNumber.getEditText().getText().toString().trim();


        if (firstName.isEmpty()){
            editFirstName.setError("First name is required!");
            editFirstName.requestFocus();
            return;
        }
        if (lastName.isEmpty()){
            editLastName.setError("Last name is required!");
            editLastName.requestFocus();
            return;
        }
        if (phone_number.isEmpty()){
            editPhoneNumber.setError("Phone number is required!");
            editPhoneNumber.requestFocus();
            return;
        }
        if (city.isEmpty()){
            editCity.setError("City is required!");
            editCity.requestFocus();
            return;
        }
        if (email.isEmpty()){
            editEmail.setError("Email is required!");
            editEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editEmail.setError("Provide a valid email!");
            editEmail.requestFocus();
            return;
        }
        if (password.isEmpty()){
            editPassword.setError("Password is required!");
            editPassword.requestFocus();
            return;
        }
        if (password.length() < 6){
            editPassword.setError("Minimum password length is 6 characters!");
            editPassword.requestFocus();
            return;
        }
        if (type==null){
            autoCompleteVehicleType.setError("please choose a vehicle type");
            autoCompleteVehicleType.requestFocus();
        }
        if (year==0){
            autoCompleteVehicleType.setError("Choose the manufactured year");
            autoCompleteVehicleType.requestFocus();
        }
        if(sCarPlateNumber.isEmpty()){
            carPlateNumber.setError("Enter your car registration number");
            carPlateNumber.requestFocus();
        }


        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            CustomVehicle vehicle=new CustomVehicle();
                            vehicle.setDriverID(FirebaseAuth.getInstance().getUid());
                            vehicle.setVehiclePlateNumber(sCarPlateNumber);
                            vehicle.setVehicleId(phone_number);
                            vehicle.setCustomVehicleType(type);
                            DriverModel driver = new DriverModel(firstName, lastName, phone_number, email, city,"",vehicle);

                            FirebaseDatabase.getInstance().getReference("Drivers")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(driver).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){

                                        Toast.makeText(RegisterActivity.this, "Successfully registered", Toast.LENGTH_LONG).show();

                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Registration failed!", Toast.LENGTH_LONG).show();

                                    }
                                    progressBar.setVisibility(View.GONE);

                                }
                            });
                        }  else {
                            Toast.makeText(RegisterActivity.this, "Registration failed!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                });
        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

            }

        }, 3000);// Time Delay ,2 Seconds
    }



}
