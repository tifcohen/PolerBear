package com.example.tiferet.polerbear.Activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.tiferet.polerbear.Fragments.ProgressFragment;
import com.example.tiferet.polerbear.R;

public class Progress extends AppCompatActivity implements ProgressFragment.ProgressFragmentDelegate{

    ProgressFragment progressFragment;
    String current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.edit_icon);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Intent intent = getIntent();
        String trickId = intent.getStringExtra("trickId");

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
    public void OnUpdateProgress() {

    }
}
