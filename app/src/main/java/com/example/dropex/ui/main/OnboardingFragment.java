package com.example.dropex.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dropex.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OnboardingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OnboardingFragment extends Fragment{


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String PAGE_NUMBER = "PAGE_NUMBER";
    private OnBoardingListener onBoardingListerner;


    interface OnBoardingListener {
            public void onNextClick();
        }







    private OnboardingFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           // mParam1 = getArguments().getString(ARG_PARAM1);
         //   mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public static OnboardingFragment newInstance(int pageNumber) {
        OnboardingFragment fragment = new OnboardingFragment();
        Bundle args = new Bundle();
        args.putInt(fragment.PAGE_NUMBER, pageNumber);
       // fragment.onBoardingListerner=onBoardingListener;
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_onboarding, container, false);
    }

    @Override
    public void onViewCreated(@NonNull  View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Bundle arguments = getArguments();
        final int pageNumber = arguments.getInt(PAGE_NUMBER);

        final ImageView imageView=view.findViewById(R.id.onboarding_image);
        final TextView heading=view.findViewById(R.id.onboarding_heading);
        final TextView body=view.findViewById(R.id.onboarding_body);
        final ExtendedFloatingActionButton startButton=view.findViewById(R.id.start_button);

        switch (pageNumber){
            case 1:
                imageView.setImageResource(R.drawable.onboarding_scene_1);
                heading.setText(R.string.onboarding_1_heading);
                body.setText(R.string.onboarding_1_body);

                break;
            case 2:
                imageView.setImageResource(R.drawable.onboarding_scene_2);
                heading.setText(R.string.onboarding_2_heading);
                body.setText(R.string.onboarding_2_body);
                break;
            case 3:
                imageView.setImageResource(R.drawable.onboarding_scene_3);
                heading.setText(R.string.onboarding_3_heading);
                body.setText(R.string.onboarding_3_body);
                break;
            default:
                //
                break;

        }


/*        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBoardingListerner != null ){
                    onBoardingListerner.onNextClick();
                }
            }
        });



 */

    }



}