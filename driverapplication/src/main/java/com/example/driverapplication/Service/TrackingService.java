package com.example.driverapplication.Service;


import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.driverapplication.JobNotification;
import com.example.driverapplication.LocationNotification;
import com.example.driverapplication.Model.DriverModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrackingService extends Service {

    private FusedLocationProviderClient mLocationProviderClient;
    private LocationCallback locationUpdatesCallback;
    private LocationRequest locationRequest;
    private static DriverModel driverModel;

    public TrackingService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Task<DataSnapshot> drivers = FirebaseDatabase.getInstance().getReference("Drivers").child(FirebaseAuth.getInstance().getUid()).get();
        drivers.addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                driverModel=dataSnapshot.getValue(DriverModel.class);
                mLocationProviderClient = LocationServices.getFusedLocationProviderClient(TrackingService.this);
                setUpLocationRequest();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference jobRef = db.collection("PostedJob").document(driverModel.getVehicle().getCustomVehicleType().getProfile().getValue()).collection("jobs");
                jobRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    Log.d("TRACKING SERVICE", "New city: " + dc.getDocument().getData());
                                    List<DocumentSnapshot> documents = value.getDocuments();
                                    String userID = documents.get(0).get("userID").toString();
                                    String jobID=documents.get(0).get("jobID").toString();
                                    String pickUpLocation=documents.get(0).get("pickUpLocation").toString();
                                    JobNotification.notify(TrackingService.this, "Job Alert",
                                            "A new Job is available at "+ pickUpLocation,userID,jobID);
                                    break;
                            }
                        }

                    }

                });
            }
        });
    }


    private void setUpLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(30000);
        locationRequest.setFastestInterval(15000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String keyValue = intent.getStringExtra("key");
        if(keyValue!=null && keyValue.equals("stop")){
            stopSelf();
        }else {

            Task<DataSnapshot> drivers = FirebaseDatabase.getInstance().getReference("Drivers").child(FirebaseAuth.getInstance().getUid()).get();
            drivers.addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    driverModel=dataSnapshot.getValue(DriverModel.class);
                    setUpLocationUpdatesCallback();
                    mLocationProviderClient.requestLocationUpdates(locationRequest, locationUpdatesCallback, null);
                }
            });
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocationNotification.cancel(this);
        mLocationProviderClient.removeLocationUpdates(locationUpdatesCallback);
    }

    private void setUpLocationUpdatesCallback() {
      /*  LocationListener locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Location lastLocation = location;
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference ref = db.collection("driverLocation").document(FirebaseAuth.getInstance().getCurrentUser().getUid());


                Map<String, Object> data = new HashMap<>();
                data.put("latitude", lastLocation.getLatitude());
                data.put("longitude", lastLocation.getLongitude());
                data.put("lastUpdatedtime", lastLocation.getTime());
                ref.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //
                        Log.i("tag", "Location update saved");
                    }
                });
                LocationNotification.notify(TrackingService.this, "Location Tracking",
                        "Lat:" + lastLocation.getLatitude() + " - Lng:" + lastLocation.getLongitude());
            }
        };*/
       locationUpdatesCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if(locationResult!=null){

                    Location lastLocation = locationResult.getLastLocation();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference ref = db.collection("driverLocation").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    CollectionReference jobRef = db.collection("PostedJob").document(driverModel.getVehicle().getCustomVehicleType().getProfile().getValue()).collection("jobs");


                    Map<String, Object> data = new HashMap<>();
                    data.put("latitude", lastLocation.getLatitude());
                    data.put("longitude", lastLocation.getLongitude());
                    data.put("lastUpdatedtime", lastLocation.getTime());
                    ref.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //
                            Log.i("tag", "Location update saved");
                        /*    LocationNotification.notify(TrackingService.this, "Location Tracking",
                                    "Lat:" + lastLocation.getLatitude() + " - Lng:" + lastLocation.getLongitude());

                         */
                        }
                    });
/*
                    jobRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                            List<DocumentSnapshot> documents = value.getDocuments();
                            String userID = documents.get(0).get("userID").toString();
                            String jobID=documents.get(0).get("jobID").toString();
                            String pickUpLocation=documents.get(0).get("pickUpLocation").toString();
                            JobNotification.notify(TrackingService.this, "Job Alert",
                                    "A new Job is available at "+ pickUpLocation,userID,jobID);


                        }
                    });

 */


                   /* LocationNotification.notify(TrackingService.this, "Location Tracking",
                            "Lat:" + lastLocation.getLatitude() + " - Lng:" + lastLocation.getLongitude());

                    */
                }else{
                    Log.i("tag", "Location null");
                }
            }
        };


    }

}