package com.example.dropex.ui.job;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dropex.R;


public class JobViewFragment extends Fragment {


    private static final String JOB_ID = "JOB_ID";

    private String mParam1;
    private String mParam2;

    public JobViewFragment() {
        // Required empty public constructor
    }

    public static JobViewFragment newInstance(String jobID) {
        JobViewFragment fragment = new JobViewFragment();
        Bundle args = new Bundle();
        args.putString(JOB_ID, jobID);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(JOB_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_job_view, container, false);
    }
}