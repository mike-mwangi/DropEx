package com.example.driverapplication.util;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public abstract class Common {
    public static String DriverReference="Drivers";


    public static DatabaseReference getDriverReference(){

        return FirebaseDatabase.getInstance().getReference("Drivers");
    }
}
