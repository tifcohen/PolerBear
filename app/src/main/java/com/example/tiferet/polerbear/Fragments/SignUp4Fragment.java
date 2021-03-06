package com.example.tiferet.polerbear.Fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.tiferet.polerbear.R;
import com.example.tiferet.polerbear.Repository.Server.User;

public class SignUp4Fragment extends Fragment {
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public interface SignUp4FragmentDelegate {
       void OnMyProfile();
   }
    public SignUp4Fragment() {
        // Required empty public constructor
    }

    SignUp4FragmentDelegate delegate;
    public void setDelegate(SignUp4FragmentDelegate delegate){this.delegate = delegate;}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_sign_up4, container, false);
        Button finishBtn = (Button) view.findViewById(R.id.finishBtn);
        Button backBtn = (Button) view.findViewById(R.id.backBtn);
        TextView level = (TextView) view.findViewById(R.id.setLevel);
        level.setText(user.getUserLevel().toString());

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delegate != null) {
                    delegate.OnMyProfile();
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//TODO debug!
                getActivity().finish();
            }
        });
        return view;
    }
}
