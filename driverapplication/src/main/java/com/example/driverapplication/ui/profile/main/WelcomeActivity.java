package com.example.driverapplication.ui.profile.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.driverapplication.DriverClient;
import com.example.driverapplication.MainActivity;
import com.example.driverapplication.Model.DriverModel;
import com.example.driverapplication.NavigationLauncherActivity;
import com.example.driverapplication.R;
import com.example.driverapplication.ui.profile.Login.LoginActivity;
import com.example.driverapplication.util.Common;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

// TODO: a splash display and a progress bar

public class WelcomeActivity extends AppCompatActivity {

    private final static Integer LOGIN_REQUEST_CODE = 1234;
    private List<AuthUI.IdpConfig> providers;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener listener;


    ProgressBar progressBar;

    private FirebaseUser user;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);







            init();
            firebaseAuth.addAuthStateListener(listener);








    }
    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        if (firebaseAuth != null && listener != null)
            firebaseAuth.removeAuthStateListener(listener);
        super.onStop();
    }


    private void init() {


        firebaseAuth = FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        listener = myFirebaseAuth -> {

            if (user != null) {
                checkUserFromFirebase();
            } else {
                showLoginLayout();
            }
        };

    }

    private void showLoginLayout() {

        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        finish();


    }

    private void handleAuthResponse(IdpResponse idpResponse) {
     if(idpResponse==null){
        // Toast.makeText(this, idpResponse.getError().getMessage(), Toast.LENGTH_SHORT).show();
     }
     else {
         user = FirebaseAuth.getInstance().getCurrentUser();
         checkUserFromFirebase();
     }
    }

    private void checkUserFromFirebase() {
        Common.getDriverReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            DriverModel driverModel = dataSnapshot.getValue(DriverModel.class);
                            ( (DriverClient)getApplicationContext()).setDriver(driverModel);

                            goToMainActivity();
                        } else {
                            showLoginLayout();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(WelcomeActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void goToMainActivity() {
        startActivity(new Intent(WelcomeActivity.this, NavigationLauncherActivity.class));
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        finish();
    }


}