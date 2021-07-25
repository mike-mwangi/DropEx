package com.example.dropex;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.sax.TextElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import static com.example.dropex.Common.Common.currentCustomer;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment{
    private LinearLayout fname;
    private LinearLayout surname;
    private LinearLayout email;
    private LinearLayout phone;
    private TextView emailStatus;
    private TextView email_text;
    TextView phone_text;
    TextView fname_text;
    TextView surname_text;
    private OnProfileListener onProfileListener;

    public void setOnProfileListener(OnProfileListener onProfileListener) {
        this.onProfileListener = onProfileListener;
    }

    public ProfileFragment() {
        // Required empty public constructor
    }


    interface OnProfileListener {
        public void onClick(View view);
    }
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fname=view.findViewById(R.id.fname);
        surname=view.findViewById(R.id.surname);
        phone=view.findViewById(R.id.phone_number);
        email=view.findViewById(R.id.email);
        emailStatus=view.findViewById(R.id.verify_email);
email_text=view.findViewById(R.id.text_email);
phone_text=view.findViewById(R.id.text_phone);
fname_text=view.findViewById(R.id.text_fname);
surname_text=view.findViewById(R.id.text_surname);

email_text.setText(currentCustomer.getEmail());
phone_text.setText(currentCustomer.getPhoneNumber());
fname_text.setText(currentCustomer.getFirstName());
surname_text.setText(currentCustomer.getLastName());


        if(FirebaseAuth.getInstance().getCurrentUser().isEmailVerified())
        {
            emailStatus.setText("Verified");
            emailStatus.setTextColor(getResources().getColor(R.color.reply_orange_300));
        }
        else {
            emailStatus.setText("Not Verified");
            emailStatus.setTextColor(getResources().getColor(R.color.reply_red_400));
        }

        setListener(fname);
        setListener(surname);
        setListener(phone);
        setListener(email);




    }
    private void setListener(View view){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( onProfileListener!= null ){
                    onProfileListener.onClick(view);
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

}