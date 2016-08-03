package com.example.tiferet.polerbear.Repository.Server;

import android.content.Context;
import android.content.Intent;

import com.example.tiferet.polerbear.Activities.Login;

/**
 * Created by TIFERET on 21-May-16.
 */
public class SessionManager {
    // Context
    private Context _context;
    private UserDetails mUserDetails;

    public SessionManager(Context context){
        this._context = context;
        mUserDetails = new UserDetails(context);
    }

    public void createLoginSession(int userId, String name, String email, String birthdate, String sex) {

        mUserDetails.setUserId(userId);
        mUserDetails.setIsLoggedIn(true);
        mUserDetails.setName(name);
        mUserDetails.setEmail(email);
        mUserDetails.setBrithdate(birthdate);
        mUserDetails.setSex(sex);
    }

    public void updateSession(String birthdate, String sex){

        if(mUserDetails.isLoggedIn() == false) {
            moveToLoginActivity();
            return;
        }

        mUserDetails.setBrithdate(birthdate);
        mUserDetails.setSex(sex);
    }

    public void updateLevelSession(int level){

        if(mUserDetails.isLoggedIn() == false) {
            moveToLoginActivity();
            return;
        }

        mUserDetails.setLevel(level);
    }

    public String getSex(){
        if(mUserDetails.isLoggedIn() == false) {
            moveToLoginActivity();
            return "";
        }
        return mUserDetails.getSex();
    }


    public String getBrithdate(){
        if(mUserDetails.isLoggedIn() == false) {
            moveToLoginActivity();
            return "";
        }
        return mUserDetails.getBrithdate();
    }

    public String getEmail(){
        if(mUserDetails.isLoggedIn() == false) {
            moveToLoginActivity();
            return "";
        }
        return mUserDetails.getEmail();
    }

    public String getName(){
        if(mUserDetails.isLoggedIn() == false) {
            moveToLoginActivity();
            return "";
        }
        return mUserDetails.getName();
    }

    public int getLevel(){
        if(mUserDetails.isLoggedIn() == false) {
            moveToLoginActivity();
            return -1;
        }
        return mUserDetails.getLevel();
    }

    public Integer getUserId(){
        if(mUserDetails.isLoggedIn() == false) {
            moveToLoginActivity();
            return -1;
        }
        return mUserDetails.getUserId();
    }

    public boolean isLoggedId() {
        return mUserDetails.isLoggedIn();
    }
    public void logoutUser(){

        mUserDetails.clearAllData();

        // After logout redirect user to Loing Activity
        moveToLoginActivity();
    }


    private void moveToLoginActivity() {
        // user is not logged in redirect him to Login Activity
        Intent i = new Intent(_context, Login.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }
}
