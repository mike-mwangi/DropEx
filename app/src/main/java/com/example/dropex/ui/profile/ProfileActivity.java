package com.example.dropex.ui.profile;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.dropex.Common.Common;
import com.example.dropex.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import static com.example.dropex.Common.Common.currentCustomer;

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
    private DatabaseReference customerInfoRef;
    private FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
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

EditProfileFragment fragment=EditProfileFragment.newInstance(getResources().getString(R.string.fname_label),currentCustomer.getFirstName(),this);
fragment.setEditListener(this);

                ft.replace(R.id.fragment_placeholder,  fragment)
                .addToBackStack(null);
                ft.commit();
                break;
            case R.id.surname:
                //   EditProfileFragment.newInstance("@string/fname_label",currentCustomer.getFirstName());
                ft.replace(R.id.fragment_placeholder,  EditProfileFragment.newInstance(getResources().getString(R.string.surname_label),currentCustomer.getLastName(),this))
                        .addToBackStack(null);

                ft.commit();
                break;
            case R.id.phone_layout:
                //   EditProfileFragment.newInstance("@string/fname_label",currentCustomer.getFirstName());
                ft.replace(R.id.fragment_placeholder,  EditProfileFragment.newInstance(getResources().getString(R.string.phone_label),currentCustomer.getPhoneNumber(),this)).addToBackStack(null);
                ft.commit();
                break;
            case R.id.email:
                //   EditProfileFragment.newInstance("@string/fname_label",currentCustomer.getFirstName());
                ft.replace(R.id.fragment_placeholder,  EditProfileFragment.newInstance(getResources().getString(R.string.email_label),currentCustomer.getEmail(),this)).addToBackStack(null);
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
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
        database = FirebaseDatabase.getInstance("https://dropex-c78c1-default-rtdb.firebaseio.com/");
        customerInfoRef = database.getReference(Common.CUSTOMER_INFO_REFERENCE);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        selectedImage.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] imagedata = baos.toByteArray();


                        StorageReference userImagesRef = storageRef.child("images/"+user.getPhoneNumber()+".jpg");
                        uploadTask = userImagesRef.putBytes(imagedata);

                        // Register observers to listen for when the download is done or if it fails
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                // ...
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
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                // ...
                                currentCustomer.setUserImageUrl(taskSnapshot.getMetadata().getPath());
                                customerInfoRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(currentCustomer)
                                        .addOnFailureListener(e -> {

                                            Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }).addOnSuccessListener(aVoid -> {
                                    Toast.makeText(ProfileActivity.this, "image updated successfully", Toast.LENGTH_SHORT).show();


                                });
                                taskSnapshot.getMetadata().getPath();
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
            database = FirebaseDatabase.getInstance("https://dropex-c78c1-default-rtdb.firebaseio.com/");
            customerInfoRef = database.getReference(Common.CUSTOMER_INFO_REFERENCE);
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