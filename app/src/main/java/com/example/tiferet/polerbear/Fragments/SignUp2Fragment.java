package com.example.tiferet.polerbear.Fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up2, container, false);
    }
}
