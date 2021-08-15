package com.example.driverapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.driverapplication.Model.DriverModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView register, login_textView;
    private EditText editFirstName, editLastName, editPhoneNumber, editEmail, editCity, editPassword;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        register = (Button) findViewById(R.id.btn_register);
        register.setOnClickListener(this);
        login_textView = (TextView) findViewById(R.id.login_textView);
        login_textView.setOnClickListener(this);

        editFirstName = (EditText) findViewById(R.id.edit_first_name);
        editLastName = (EditText) findViewById(R.id.edit_last_name);
        editPhoneNumber = (EditText) findViewById(R.id.edit_phone_number);
        editEmail = (EditText) findViewById(R.id.edit_email);
        editCity = (EditText) findViewById(R.id.edit_city);
        editPassword = (EditText) findViewById(R.id.edit_password);
        login_textView = (TextView) findViewById(R.id.login_textView);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_textView:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.btn_register:
                registerUser();
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

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            DriverModel driver = new DriverModel(firstName, lastName, phone_number, email, city);

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


    }
}