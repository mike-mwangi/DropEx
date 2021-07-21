package com.example.dropex;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.dropex.Common.Common;
import com.example.dropex.Model.CustomerModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    DatabaseReference customerInfoRef;
    private Context context;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);


        TextInputEditText edit_first_name = (TextInputEditText)view.findViewById(R.id.edit_first_name);
        TextInputEditText edit_last_name = (TextInputEditText)view.findViewById(R.id.edit_last_name);
        TextInputEditText edit_phone = (TextInputEditText)view.findViewById(R.id.edit_phone_number);
        TextInputEditText edit_email = (TextInputEditText)view.findViewById(R.id.edit_email);

        Button btn_continue = (Button)view.findViewById(R.id.btn_register);

        if (FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber() != null && !TextUtils.
                isEmpty(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()))
            //Populate field phone number with users number
            edit_phone.setText(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());

        builder.setView(view);
        AlertDialog dialog = builder.create();


        btn_continue.setOnClickListener(vieww -> {
            if (TextUtils.isEmpty(edit_first_name.getText().toString())) {
                Toast.makeText(context, "Please enter first name", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(edit_last_name.getText().toString())) {
                Toast.makeText(context, "Please enter last name", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(edit_phone.getText().toString())) {
                Toast.makeText(context, "Please enter mobile number", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(edit_email.getText().toString())) {
                Toast.makeText(context, "Please enter email", Toast.LENGTH_SHORT).show();
                return;
            } else {

                CustomerModel model = new CustomerModel();
                model.setFirstName(edit_first_name.getText().toString());
                model.setLastName(edit_last_name.getText().toString());
                model.setPhoneNumber(edit_phone.getText().toString());
                model.setEmail(edit_email.getText().toString());

               FirebaseDatabase database = FirebaseDatabase.getInstance("https://dropex-c78c1-default-rtdb.firebaseio.com/");
                customerInfoRef = database.getReference(Common.CUSTOMER_INFO_REFERENCE);
                customerInfoRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(model)
                        .addOnFailureListener(e -> {
                            dialog.dismiss();
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }).addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Registered successfully", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    goToHomeActivity(model);
                });

            }
        });

    }

    private void goToHomeActivity(CustomerModel customerModel) {
        Common.currentCustomer = customerModel;
        startActivity(new Intent(context, HomeActivity.class));

    }
}