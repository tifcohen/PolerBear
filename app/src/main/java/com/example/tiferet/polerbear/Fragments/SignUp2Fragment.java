package com.example.tiferet.polerbear.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.tiferet.polerbear.R;
import com.example.tiferet.polerbear.Repository.Server.User;


public class SignUp2Fragment extends Fragment {

    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public interface SignUp2FragmentDelegate{
        void OnSignUp3(User user);
        void OnCancel();

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

        final EditText email = (EditText) view.findViewById(R.id.Join2EmailEditText);
        EditText firstName = (EditText) view.findViewById(R.id.addUserFName);
        EditText lastName = (EditText) view.findViewById(R.id.addUserLName);
        final EditText birthdate = (EditText) view.findViewById(R.id.addUserBirthDate);
        final Spinner sexDropdown = (Spinner) view.findViewById(R.id.dropdown);
        final ArrayAdapter<String>sexAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, sexPickerDropdown);
        sexDropdown.setAdapter(sexAdapter);

        Button nextBtn = (Button) view.findViewById(R.id.nextBtn);
        Button cancelBtn = (Button) view.findViewById(R.id.cancelBtn);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delegate != null) {
                    user.setUserEmail(email.getText().toString());
                    user.setUserBirthDate(birthdate.getText().toString());
                    user.setUserSex(sexDropdown.getSelectedItem().toString());
                    delegate.OnSignUp3(user);
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegate.OnCancel();
            }
        });

        return view;
    }
}
