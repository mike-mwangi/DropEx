package com.example.dropex.ui.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.dropex.Common.Common;
import com.example.dropex.Model.CustomerModel;
import com.example.dropex.NavigationLauncherActivity;
import com.example.dropex.R;
import com.example.dropex.SharedPref;
import com.example.dropex.UserClient;
import com.example.dropex.ui.home.HomeActivity;
import com.example.dropex.ui.shipments.main.CallToActionActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import butterknife.ButterKnife;

// TODO: a splash display and a progress bar

public class SplashScreenActivity extends AppCompatActivity {

    private final static Integer LOGIN_REQUEST_CODE = 1234;
    private List<AuthUI.IdpConfig> providers;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener listener;


    ProgressBar progressBar;

    FirebaseDatabase database;
    DatabaseReference customerInfoRef;
    private FirebaseUser user;
    private final ActivityResultLauncher intentActivityResultLauncher = registerForActivityResult(new AuthResultContract(),
            new ActivityResultCallback<IdpResponse>() {
                @Override
                public void onActivityResult(IdpResponse result) {
                    handleAuthResponse(result);
                }
            });




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        progressBar=findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);



        if(isFirstLaunch()) {
            startActivity(new Intent(this, OnboardingActivity.class));
            finish();
        }
        else {


            init();
            firebaseAuth.addAuthStateListener(listener);
        }







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
        ButterKnife.bind(this);

        database = FirebaseDatabase.getInstance("https://dropex-c78c1-default-rtdb.firebaseio.com/");
        customerInfoRef = database.getReference(Common.CUSTOMER_INFO_REFERENCE);



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

        intentActivityResultLauncher.launch(LOGIN_REQUEST_CODE);


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
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                user = FirebaseAuth.getInstance().getCurrentUser();
            } else {
                Toast.makeText(this, response.getError().getMessage(), Toast.LENGTH_SHORT).show();
            }

    }

 */

    private void checkUserFromFirebase() {
        customerInfoRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            CustomerModel customerModel = dataSnapshot.getValue(CustomerModel.class);
                            ((UserClient)getApplicationContext()).setCustomer(customerModel);

                            goToHomeActivity();
                        } else {
                            showRegisterLayout();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(SplashScreenActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void showRegisterLayout() {
        Context context;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_placeholder, new RegisterFragment());
        ft.commit();
        

    }

    private void goToHomeActivity() {

        startActivity(new Intent(SplashScreenActivity.this, NavigationLauncherActivity.class));
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        finish();
    }
    private Boolean isFirstLaunch() {
        return SharedPref.getInstance(this.getApplicationContext()).isFirstLaunch();
    }

}