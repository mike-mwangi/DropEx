package com.example.driverapplication.ui.profile.Login;

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

import com.example.driverapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private  EditText emailEditText;
    private Button resetPasswordBtn;
    private ProgressBar progressBar;
    private TextView backToLoginTextView;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailEditText = (EditText) findViewById(R.id.edit_email);

        resetPasswordBtn = (Button) findViewById(R.id.btn_forgot_password);
        resetPasswordBtn.setOnClickListener(this);

        backToLoginTextView = (TextView) findViewById(R.id.backToLoginTextView);
        backToLoginTextView.setOnClickListener(this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        auth = FirebaseAuth.getInstance();



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_forgot_password:
                resetPassword();
                break;
            case R.id.backToLoginTextView:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;

        }
    }


    private void resetPassword() {
        String email = emailEditText.getText().toString().trim();

        if (email.isEmpty()){
            emailEditText.setError("Email is required!");
            emailEditText.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Provide a valid email!");
            emailEditText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Check your email to reset your password!", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Try again!", Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);

            }
        });

    }

}