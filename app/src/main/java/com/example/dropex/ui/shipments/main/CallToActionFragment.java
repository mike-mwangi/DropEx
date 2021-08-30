package com.example.dropex.ui.shipments.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dropex.NavigationLauncherActivity;
import com.example.dropex.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CallToActionFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class CallToActionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private CardView sendPackage;
    private TextInputLayout verificationCode;
    private MaterialButton verifyCode;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CallToActionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CallToActionFragment newInstance(String param1, String param2) {
        CallToActionFragment fragment = new CallToActionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CallToActionFragment() {
        // Required empty public constructor
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
        return inflater.inflate(R.layout.fragment_call_to_action, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sendPackage=view.findViewById(R.id.send_package);
        verificationCode=view.findViewById(R.id.recieve_package_code_input);
        verifyCode=view.findViewById(R.id.trackLocationBtn);
        verifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                connectToDriver(verificationCode.getEditText().getText().toString());

            }
        });
        //  receivePackage=findViewById(R.id.receive_package);
        sendPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action =
                        CallToActionFragmentDirections.actionCTAFragmentToJobInformation();
                Navigation.findNavController(v).navigate(action);
            }
        });
    }

    private void connectToDriver(String code) {
     /*   Task<DocumentSnapshot> documentSnapshotTask = FirebaseFirestore.getInstance().collection("on-going-jobs").document(code).get();
        documentSnapshotTask.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String driverID = documentSnapshot.get("driverID").toString();
                Intent goToMap=new Intent(CallToActionFragment.this.getContext(), NavigationLauncherActivity.class);
                goToMap.putExtra("driverID",driverID);

            }
        });

      */
        Intent connectToDriver=new Intent(getActivity(),NavigationLauncherActivity.class);
        connectToDriver.putExtra("driverID",code);
        startActivity(connectToDriver);
    }
}