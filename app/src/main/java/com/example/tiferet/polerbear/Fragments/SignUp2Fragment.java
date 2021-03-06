package com.example.tiferet.polerbear.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.tiferet.polerbear.API.IUserAPI;
import com.example.tiferet.polerbear.R;
import com.example.tiferet.polerbear.Repository.Server.Repository;
import com.example.tiferet.polerbear.Repository.Server.SessionManager;
import com.example.tiferet.polerbear.Repository.Server.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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

    SessionManager session;
    String[] sexPickerDropdown = new String[]{"Male", "Female"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up2, container, false);

        final IUserAPI api = Repository.getInstance().retrofit.create(IUserAPI.class);
        session = new SessionManager(getActivity().getApplicationContext());

        final EditText email = (EditText) view.findViewById(R.id.Join2EmailEditText);
        final EditText birthdate = (EditText) view.findViewById(R.id.addUserBirthDate);
        final Spinner sexDropdown = (Spinner) view.findViewById(R.id.dropdown);
        final ArrayAdapter<String>sexAdapter = new ArrayAdapter<String>(getActivity(), R.layout.dropdown_item_selected, sexPickerDropdown);
        sexAdapter.setDropDownViewResource(R.layout.dropdown_items);
        sexDropdown.setAdapter(sexAdapter);

        Button nextBtn = (Button) view.findViewById(R.id.nextBtn);
        Button cancelBtn = (Button) view.findViewById(R.id.cancelBtn);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEmailValid(email.getText())){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setTitle("Action Failed");
                    alertDialogBuilder.setMessage("Please enter a valid email").setCancelable(false)
                            .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    alertDialog.getWindow().setDimAmount(0.5f);
                    alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                }
                else {
                    user.setUserEmail(email.getText().toString());
                    user.setUserBirthDate(birthdate.getText().toString());
                    user.setUserSex(sexDropdown.getSelectedItem().toString());
                    //user.setUserId(-1);
                    //user.setUserLevel(-1);
                    //final Call<Integer> call = api.addUser(user.getUserName(),user.getUserPwd(),user.getUserEmail(),user.getUserBirthDate(),user.getUserSex());
                    final Call<Integer> call = api.addUser("application/json", user);

                    call.enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            Log.d("id", response.body()+"");
                            Log.d("id", user.getUserName());
                            Log.d("id", user.getUserEmail());
                            Log.d("id", user.getUserBirthDate());
                            Log.d("id", user.getUserSex());
                            session.createLoginSession(response.body(), user.getUserName(), user.getUserEmail(), user.getUserBirthDate(), user.getUserSex());
                            Log.d("TAG", "pass addUser "+response.body());
                            if (delegate != null) {
                                user.setUserId(response.body());
                                delegate.OnSignUp3(user);
                            }
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {
                            Log.d("TAG", "signup failed " + call.toString());
                        }
                    });
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

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
