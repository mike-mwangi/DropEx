package com.example.dropex.ui.job;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.example.dropex.Model.Job;
import com.example.dropex.R;
import com.example.dropex.UserClient;
import com.example.dropex.ui.main.RegisterFragment;
import com.example.dropex.ui.profile.ProfileFragment;
import com.example.dropex.ui.shipments.main.CallToActionActivity;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class JobsActivity extends AppCompatActivity {


    private JobsRecyclerViewAdapter adapter;
    private FrameLayout container;
    private FragmentTransaction ft;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);


        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.job_nav_host_fragment);
        navController = navHostFragment.getNavController();



    }

    public void goToCallToAction(View view) {
        startActivity(new Intent(this, CallToActionActivity.class));
    }


}