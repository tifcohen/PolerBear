package com.example.tiferet.polerbear.Activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.tiferet.polerbear.Fragments.SignUp2Fragment;
import com.example.tiferet.polerbear.Fragments.SignUp3Fragment;
import com.example.tiferet.polerbear.Fragments.SignUp4Fragment;
import com.example.tiferet.polerbear.Fragments.SignUpFragment;
import com.example.tiferet.polerbear.R;
import com.example.tiferet.polerbear.Repository.Server.User;

public class SignUp extends AppCompatActivity implements SignUpFragment.SignUpFragmentDelegate, SignUp2Fragment.SignUp2FragmentDelegate,
        SignUp3Fragment.SignUp3FragmentDelegate, SignUp4Fragment.SignUp4FragmentDelegate{

    SignUpFragment signUpFragment;
    SignUp2Fragment signUp2Fragment;
    SignUp3Fragment signUp3Fragment;
    SignUp4Fragment signUp4Fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setTitle("Sign Up Process");
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
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) { /// TODO!!!
            case android.R.id.home:
                FragmentManager fm = getFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }
                else{
                    onBackPressed();
                }
                break;
        }

        return true;
    }

    @Override
    public void OnSignUp2(User user) {
        signUp2Fragment = new SignUp2Fragment();
        signUp2Fragment.setDelegate(this);
        signUp2Fragment.setUser(user);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.signupContainer, signUp2Fragment);
        ft.hide(signUpFragment);
        ft.commit();
        invalidateOptionsMenu();
    }

    @Override
    public void OnSignUp3(User user) {
        signUp3Fragment = new SignUp3Fragment();
        signUp3Fragment.setDelegate(this);
        signUp3Fragment.setUser(user);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.signupContainer, signUp3Fragment);
        ft.hide(signUp2Fragment);
        ft.commit();
        invalidateOptionsMenu();
    }

    @Override
    public void OnSignUp4(User user) {
        signUp4Fragment = new SignUp4Fragment();
        signUp4Fragment.setDelegate(this);
        signUp4Fragment.setUser(user);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.signupContainer, signUp4Fragment);
        ft.hide(signUp3Fragment);
        ft.commit();
        invalidateOptionsMenu();
    }

    @Override
    public void OnMyProfile() {
        Intent intent = new Intent(getApplicationContext(), MyProfile.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void OnCancel() {
        invalidateOptionsMenu();
        getFragmentManager().popBackStack();
        onBackPressed();
    }
}