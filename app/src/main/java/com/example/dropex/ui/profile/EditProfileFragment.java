package com.example.dropex.ui.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.dropex.R;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends Fragment {

    int id=10101;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String FIELD_NAME = "FIELD_NAME";
    private static final String FIELD_VALUE = "FIELD_VALUE";
    private TextInputEditText editText;
    private Button saveButton;


    // TODO: Rename and change types of parameters
    private String fieldName;
    private String fieldValue;
    public static EditListener editListener;

    interface EditListener{
        public void onClickSave(String field,String value);
    }

    public  void setEditListener(EditProfileFragment.EditListener editListenerListener) {
        this.editListener = editListener;
    }

    public EditProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param fieldName Parameter 1.
     * @param fieldValue Parameter 2.
     * @return A new instance of fragment EditProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProfileFragment newInstance(String fieldName, String fieldValue,EditListener editListener1) {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        args.putString(FIELD_NAME, fieldName);
        args.putString(FIELD_VALUE, fieldValue);
        fragment.setArguments(args);
        editListener=editListener1;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fieldName = getArguments().getString(FIELD_NAME);
            fieldValue = getArguments().getString(FIELD_VALUE);
        }

    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editText=view.findViewById(R.id.input);
        editText.setText(fieldValue);
        saveButton=view.findViewById(R.id.save_btn);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( editListener!= null ){
                    editListener.onClickSave(fieldName,editText.getText().toString());
                }
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }
}