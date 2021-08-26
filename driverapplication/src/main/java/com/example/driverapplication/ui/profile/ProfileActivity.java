package com.example.driverapplication.ui.profile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.driverapplication.DriverClient;
import com.example.driverapplication.Model.DriverModel;
import com.example.driverapplication.R;
import com.example.driverapplication.util.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;


// TODO: implement the CRUD functionality

public class ProfileActivity extends AppCompatActivity implements ProfileFragment.OnProfileListener, EditProfileFragment.EditListener {


    private FirebaseUser user;
    FirebaseStorage  storage = FirebaseStorage.getInstance();
    StorageReference  storageReference = storage.getReference();
    public String TAG=getPackageName();
    private TextInputEditText editText;
    private FragmentTransaction ft;
    private UploadTask uploadTask;
    private StorageReference storageRef;
    private DriverModel driverModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        driverModel=((DriverClient)getApplicationContext()).getDriver();

        ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(
                R.anim.slide_in,  // enter
                R.anim.fade_out,  // exit
                R.anim.fade_in,   // popEnter
                R.anim.slide_out  // popExit
        );
        ProfileFragment profileFragment=ProfileFragment.newInstance();
        profileFragment.setOnProfileListener(this);
        ft.replace(R.id.fragment_placeholder, profileFragment );

        ft.commit();

        user = FirebaseAuth.getInstance().getCurrentUser();
        editText=findViewById(R.id.input);




    }



    @Override
    public void onClick(View v) {

        ft = getSupportFragmentManager().beginTransaction();
        switch (v.getId()){
            case R.id.fname:

        EditProfileFragment fragment=EditProfileFragment.newInstance(getResources().getString(R.string.fname_label),driverModel.getFirstName(),this);
        fragment.setEditListener(this);

                ft.replace(R.id.fragment_placeholder,  fragment)
                .addToBackStack(null);
                ft.commit();
                break;
            case R.id.surname:
                //   EditProfileFragment.newInstance("@string/fname_label",currentCustomer.getFirstName());
                ft.replace(R.id.fragment_placeholder,  EditProfileFragment.newInstance(getResources().getString(R.string.surname_label),driverModel.getLastName(),this))
                        .addToBackStack(null);

                ft.commit();
                break;
            case R.id.phone_layout:
                //   EditProfileFragment.newInstance("@string/fname_label",currentCustomer.getFirstName());
                ft.replace(R.id.fragment_placeholder,  EditProfileFragment.newInstance(getResources().getString(R.string.phone_label),driverModel.getPhoneNumber(),this)).addToBackStack(null);
                ft.commit();
                break;
            case R.id.email:
                //   EditProfileFragment.newInstance("@string/fname_label",currentCustomer.getFirstName());
                ft.replace(R.id.fragment_placeholder,  EditProfileFragment.newInstance(getResources().getString(R.string.email_label),driverModel.getEmail(),this)).addToBackStack(null);
                ft.commit();
                break;
            case R.id.user_image:
                selectImage(this);
                break;



        }
    }

    private void selectImage(Context context) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);


                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        storageRef = storage.getReference();
        if (resultCode != RESULT_CANCELED) {

            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        selectedImage.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] imagedata = baos.toByteArray();


                        StorageReference userImagesRef = storageRef.child("user/profile-images/"+user.getPhoneNumber()+".jpg");
                        uploadTask = userImagesRef.putBytes(imagedata);

                        // Register observers to listen for when the download is done or if it fails
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                Log.e("uploading photo",exception.toString());
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                // ...
                              //  final Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl();

                              //  currentCustomer.setUserImageUrl(userImagesRef.getDownloadUrl());

                                userImagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Map<String, Object> hopperUpdates = new HashMap<>();
                                        hopperUpdates.put("userImageUrl",uri.toString());
                                        Common.getDriverReference().child(user.getUid()).updateChildren(hopperUpdates);
//


                                    }
                                });


                            }
                        });
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        
                        StorageReference riversRef = storageRef.child("user/profile-images/"+user.getPhoneNumber()+".jpg");
                        uploadTask = riversRef.putFile(selectedImage);

// Register observers to listen for when the download is done or if it fails
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                            }
                        });
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Map<String, Object> hopperUpdates = new HashMap<>();
                                        hopperUpdates.put("userImageUrl",uri.toString());
                                        Common.getDriverReference().child(user.getUid()).updateChildren(hopperUpdates);
//

                                    }
                                });


                            }
                        });

                    }
                    break;
            }
        }
    }

    @Override
    public void onClickSave(String field,String value) {
        if(field== getResources().getString(R.string.fname_label)){

            driverModel.setFirstName(value);
            Common.getDriverReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(driverModel)
                    .addOnFailureListener(e -> {

                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }).addOnSuccessListener(aVoid -> {
                Toast.makeText(this, "updated successfully", Toast.LENGTH_SHORT).show();


            });
        }
        else if(field == getResources().getString(R.string.surname_label)){

            driverModel.setLastName(value);
            Common.getDriverReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(driverModel)
                    .addOnFailureListener(e -> {

                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }).addOnSuccessListener(aVoid -> {
                Toast.makeText(this, "updated successfully", Toast.LENGTH_SHORT).show();


            });
        }
        else if(field == getResources().getString(R.string.email_label)){

            driverModel.setEmail(value);

            Common.getDriverReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(driverModel)
                    .addOnFailureListener(e -> {

                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }).addOnSuccessListener(aVoid -> {
                user.sendEmailVerification();
                Toast.makeText(this, "updated successfully", Toast.LENGTH_SHORT).show();



            });
        }
    }
}