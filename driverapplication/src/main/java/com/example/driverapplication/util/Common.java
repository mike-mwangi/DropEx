package com.example.driverapplication.util;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public abstract class Common {
    public static String DriverReference="Drivers";



    public static DatabaseReference getDriverReference(){

        return FirebaseDatabase.getInstance().getReference("Drivers");
    }

}
