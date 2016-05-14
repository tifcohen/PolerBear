package com.example.tiferet.polerbear.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.tiferet.polerbear.API.IUserAPI;
import com.example.tiferet.polerbear.R;
import com.example.tiferet.polerbear.Repository.Server.Repository;
import com.example.tiferet.polerbear.Repository.Server.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpFragment extends Fragment {
    public interface SignUpFragmentDelegate{
        void OnSignUp2(User user);
        void OnCancel();
    }

    User newUser;
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
        Button cancelBtn = (Button) view.findViewById(R.id.cancelBtn);
        final EditText user = (EditText) view.findViewById(R.id.join1UsernameEditText);
        final EditText pwd = (EditText) view.findViewById(R.id.join1PasswordEditText);
        final EditText repwd = (EditText) view.findViewById(R.id.join1RepeatPasswordEditText);

        IUserAPI api = Repository.getInstance().retrofit.create(IUserAPI.class);
        final Call<Boolean> call = api.isExisted(user.getText().toString(),"michaelkolet@gmail.com",pwd.getText().toString());

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.getText().toString().equals("") || pwd.getText().toString().equals("")){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setTitle("Action Failed");
                    alertDialogBuilder.setMessage("Please fill the requested fields").setCancelable(false)
                            .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    alertDialog.getWindow().setDimAmount(0.5f);
                    alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                }
                else if (pwd.getText().toString().equals(repwd.getText().toString())) {
                    call.enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if (!response.body()){
                                if (delegate != null) {
                                    newUser = new User();
                                    newUser.setUserName(user.getText().toString());
                                    newUser.setUserPwd(pwd.getText().toString());
                                    delegate.OnSignUp2(newUser);
                                }
                            }
                            else{
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                                alertDialogBuilder.setTitle("Action Failed");
                                alertDialogBuilder.setMessage("A user with that name is already exists").setCancelable(false)
                                        .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                            }
                                        });
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                                alertDialog.getWindow().setDimAmount(0.5f);
                                alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                            }
                        }
                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                        }
                    });
                }
                else{
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setTitle("Action Failed");
                    alertDialogBuilder.setMessage("Passwords don't match").setCancelable(false)
                            .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    alertDialog.getWindow().setDimAmount(0.5f);
                    alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
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
