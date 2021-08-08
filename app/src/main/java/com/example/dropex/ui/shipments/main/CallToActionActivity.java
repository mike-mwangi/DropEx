package com.example.dropex.ui.shipments.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;

import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.dropex.R;
import com.google.firebase.auth.FirebaseUser;

// TODO: call to action receive a package,send a package

public class CallToActionActivity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseUser user;
    private CardView sendPackage;
    private CardView receivePackage;
    private CardView discountsAndOffers;
    private NavController navController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_to_action);
      
      //  receivePackage=findViewById(R.id.receive_package);



        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();



    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.send_package:

                NavDirections action =
                        CallToActionFragmentDirections.actionCTAFragmentToJobInformation();
                Navigation.findNavController(v).navigate(action);

                break;
           /* case R.id.receive_package:
                showTrackingNoInput();
                break;
            case R.id.btn_track:
                break;

            */
        }
    }

}