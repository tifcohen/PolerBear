package com.example.tiferet.polerbear.Activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.tiferet.polerbear.Fragments.ProgressFragment;
import com.example.tiferet.polerbear.Fragments.UpdateProgressFragment;
import com.example.tiferet.polerbear.R;
import com.example.tiferet.polerbear.Repository.Server.SessionManager;

import java.util.HashMap;

public class Progress extends AppCompatActivity implements ProgressFragment.ProgressFragmentDelegate, UpdateProgressFragment.UpdateProgressFragmentDelegate {

    ProgressFragment progressFragment;
    UpdateProgressFragment updateProgressFragment;
    String current;
    SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        session = new SessionManager(getApplicationContext());
        final HashMap<String, String> user = session.getUserDetails();

        Intent intent = getIntent();
        final String trickId = intent.getStringExtra("trickId");

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.edit_icon);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setVisibility(View.GONE);
                OnUpdateProgress(user.get(SessionManager.KEY_ID), trickId);
            }
        });

        current = "progress";
        progressFragment = new ProgressFragment();
        progressFragment.setDelegate(this);
        progressFragment.setTrickId(trickId);
        //stack.push(myProfileFragment);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.progressContainer, progressFragment);
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
    public void OnUpdateProgress(String userId, String trickId) {
        updateProgressFragment = new UpdateProgressFragment();
        //updateProgressFragment.setDelegate(this);
        updateProgressFragment.setUserId(userId);
        updateProgressFragment.setTrickId(trickId);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.progressContainer, updateProgressFragment);
        ft.hide(progressFragment);
        ft.commit();
        invalidateOptionsMenu();
    }

}
