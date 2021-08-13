package com.example.dropex.Common;

import androidx.annotation.NonNull;

import com.example.dropex.Model.CustomerModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

public class Common {
    public static final String CUSTOMER_INFO_REFERENCE = "Customers";
    public static CustomerModel currentCustomer;
  public static FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://dropex-c78c1-default-rtdb.firebaseio.com/");
  public static DatabaseReference customerDataBaseReference=firebaseDatabase.getReference(Common.CUSTOMER_INFO_REFERENCE);
    public static FirebaseStorage firebaseStorageInstance= FirebaseStorage.getInstance();
    public static StorageReference storageReference = firebaseStorageInstance.getReference();
    public static FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public static StorageReference userImageStorageReference=storageReference.child("user/profile-images/"+user.getPhoneNumber()+".jpg");


    public static String buildWelcomeMessage() {
        //Review code here
        if (Common.currentCustomer != null) {
            return String.valueOf(new StringBuilder("Welcome")
                    .append(Common.currentCustomer.getFirstName())
                    .append(" ")
                    .append(Common.currentCustomer.getLastName().toString()));
        } else {

        }
            return "";

    }
    public static CustomerModel getCurrentCustomer(){
        customerDataBaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            currentCustomer = dataSnapshot.getValue(CustomerModel.class);


                        }

                    }
                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });


           return currentCustomer;
                }
}
