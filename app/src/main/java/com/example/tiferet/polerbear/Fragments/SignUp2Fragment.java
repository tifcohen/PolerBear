package com.example.tiferet.polerbear.Fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.tiferet.polerbear.R;


public class SignUp2Fragment extends Fragment {

    public interface SignUp2FragmentDelegate{
        void OnSignUp3();
    }

    private SignUp2FragmentDelegate delegate;
    public void setDelegate(SignUp2FragmentDelegate delegate){this.delegate = delegate;}

    public SignUp2Fragment() {
        // Required empty public constructor
    }

    String[] sexPickerDropdown = new String[]{"Male", "Female"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up2, container, false);

        final Spinner sexDropdown = (Spinner) view.findViewById(R.id.dropdown);
        final ArrayAdapter<String>sexAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, sexPickerDropdown);
        sexDropdown.setAdapter(sexAdapter);

        Button nextBtn = (Button) view.findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delegate != null) {
                    delegate.OnSignUp3();
                }
            }
        });

        return view;
    }
}
