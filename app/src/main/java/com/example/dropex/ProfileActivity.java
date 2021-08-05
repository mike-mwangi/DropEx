package com.example.dropex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.dropex.Common.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.dropex.Common.Common.currentCustomer;

// TODO: implement the CRUD functionality

public class ProfileActivity extends AppCompatActivity implements ProfileFragment.OnProfileListener , EditProfileFragment.EditListener {

    private FirebaseUser user;
    public String TAG=getPackageName();
    private TextInputEditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ProfileFragment profileFragment=ProfileFragment.newInstance();
        profileFragment.setOnProfileListener(this);
        ft.replace(R.id.fragment_placeholder, profileFragment );
        ft.commit();

        user = FirebaseAuth.getInstance().getCurrentUser();
        editText=findViewById(R.id.input);
    }



    @Override
    public void onClick(View v) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (v.getId()){
            case R.id.user_image:


                break;
            case R.id.fname:
             //   EditProfileFragment.newInstance("@string/fname_label",currentCustomer.getFirstName());
EditProfileFragment fragment=EditProfileFragment.newInstance(getResources().getString(R.string.fname_label),currentCustomer.getFirstName(),this);
fragment.setEditListener(this);
                ft.replace(R.id.fragment_placeholder,  fragment);
                ft.commit();
                break;
            case R.id.surname:
                //   EditProfileFragment.newInstance("@string/fname_label",currentCustomer.getFirstName());
                ft.replace(R.id.fragment_placeholder,  EditProfileFragment.newInstance(getResources().getString(R.string.surname_label),currentCustomer.getLastName(),this));
                ft.commit();
                break;
            case R.id.phone_layout:
                //   EditProfileFragment.newInstance("@string/fname_label",currentCustomer.getFirstName());
                ft.replace(R.id.fragment_placeholder,  EditProfileFragment.newInstance(getResources().getString(R.string.phone_label),currentCustomer.getPhoneNumber(),this));
                ft.commit();
                break;
            case R.id.email:
                //   EditProfileFragment.newInstance("@string/fname_label",currentCustomer.getFirstName());
                ft.replace(R.id.fragment_placeholder,  EditProfileFragment.newInstance(getResources().getString(R.string.email_label),currentCustomer.getEmail(),this));
                ft.commit();
                break;


        }
    }


    @Override
    public void onClickSave(String field,String value) {
        if(field== getResources().getString(R.string.fname_label)){
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://dropex-c78c1-default-rtdb.firebaseio.com/");
            DatabaseReference customerInfoRef = database.getReference(Common.CUSTOMER_INFO_REFERENCE);
            currentCustomer.setFirstName(value);
            customerInfoRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(currentCustomer)
                    .addOnFailureListener(e -> {

                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }).addOnSuccessListener(aVoid -> {
                Toast.makeText(this, "updated successfully", Toast.LENGTH_SHORT).show();


            });
        }
        else if(field == getResources().getString(R.string.surname_label)){
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://dropex-c78c1-default-rtdb.firebaseio.com/");
            DatabaseReference customerInfoRef = database.getReference(Common.CUSTOMER_INFO_REFERENCE);
            currentCustomer.setLastName(value);
            customerInfoRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(currentCustomer)
                    .addOnFailureListener(e -> {

                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }).addOnSuccessListener(aVoid -> {
                Toast.makeText(this, "updated successfully", Toast.LENGTH_SHORT).show();


            });
        }
        else if(field == getResources().getString(R.string.email_label)){
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://dropex-c78c1-default-rtdb.firebaseio.com/");
            DatabaseReference customerInfoRef = database.getReference(Common.CUSTOMER_INFO_REFERENCE);
            currentCustomer.setEmail(value);
            user.sendEmailVerification();
            customerInfoRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(currentCustomer)
                    .addOnFailureListener(e -> {

                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }).addOnSuccessListener(aVoid -> {
                Toast.makeText(this, "updated successfully", Toast.LENGTH_SHORT).show();


            });
        }
    }
}