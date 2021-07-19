package com.example.dropex;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.common.internal.service.Common;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dropex.databinding.ActivityHomeBinding;
import com.google.firebase.auth.FirebaseAuth;

import static com.example.dropex.Common.Common.buildWelcomeMessage;
import static com.google.android.gms.common.internal.service.Common.*;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private NavController navController;
    private ImageView img_avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        init();
    }

    private void init() {
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_sign_out) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle("Sign out")
                        .setMessage("Are you sure you want to sign out>")
                        .setNegativeButton("CANCEL", (dialogInterface, i) -> dialogInterface.dismiss())
                        .setPositiveButton("SIGN OUT", (dialogInterface, i) -> {
                            FirebaseAuth.getInstance().signOut();
                            Intent intent = new Intent(HomeActivity.this, SplashScreenActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }).setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.setOnShowListener(dialogInterface -> {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                            .setTextColor(ContextCompat.getColor(HomeActivity.this, android.R.color.holo_red_dark));
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                            .setTextColor(ContextCompat.getColor(HomeActivity.this, R.color.colorPrimary));
                });

                dialog.show();
            }

            return true;
        });

        //Populate user data
        View headerView = navigationView.getHeaderView(0);
        TextView text_name = (TextView)headerView.findViewById(R.id.text_name);
        TextView text_phone = (TextView)headerView.findViewById(R.id.text_phone);


        img_avatar = (ImageView)headerView.findViewById(R.id.img_avatar);

        text_name.setText(buildWelcomeMessage());
        text_phone.setText(com.example.dropex.Common.Common.currentCustomer != null ? com.example.dropex.Common.Common.currentCustomer.getPhoneNumber() : "");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}