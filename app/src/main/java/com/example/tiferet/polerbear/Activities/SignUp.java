package com.example.tiferet.polerbear.Activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.tiferet.polerbear.Fragments.SignUp2Fragment;
import com.example.tiferet.polerbear.Fragments.SignUpFragment;
import com.example.tiferet.polerbear.R;

public class SignUp extends AppCompatActivity implements SignUpFragment.SignUpFragmentDelegate, SignUp2Fragment.SignUp2FragmentDelegate{

    SignUpFragment signUpFragment;
    SignUp2Fragment signUp2Fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.signupToolbar);

        //this.setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        signUpFragment = new SignUpFragment();
        signUpFragment.setDelegate(this);
        //stack.push(myProfileFragment);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.signupContainer, signUpFragment);
        ft.commit();
        invalidateOptionsMenu();
    }

    @Override
    public void OnSignUp3() {

    }

    @Override
    public void OnSignUp2() {
        signUp2Fragment = new SignUp2Fragment();
        signUp2Fragment.setDelegate(this);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.signupContainer, signUp2Fragment);
        ft.hide(signUpFragment);
        ft.commit();
        invalidateOptionsMenu();
    }
}