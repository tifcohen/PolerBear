package com.example.tiferet.polerbear.Fragments;

import android.app.Activity;
import android.net.Uri;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.tiferet.polerbear.R;

public class SignUpFragment extends Fragment {
    public interface SignUpFragmentDelegate{
        void OnSignUp2();
    }

    private SignUpFragmentDelegate delegate;
    public void setDelegate(SignUpFragmentDelegate delegate){this.delegate = delegate;}

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        Button nextBtn = (Button) view.findViewById(R.id.nextBtn);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(),"toast", Toast.LENGTH_LONG).show();
                if(delegate!=null){
                    delegate.OnSignUp2();
                }
            }
        });
        return view;
    }

}
