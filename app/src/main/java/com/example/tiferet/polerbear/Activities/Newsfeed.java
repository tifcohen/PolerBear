package com.example.tiferet.polerbear.Activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tiferet.polerbear.Classes.User;
import com.example.tiferet.polerbear.Classes.UserDB;
import com.example.tiferet.polerbear.Fragments.GlobalNewsfeedFragment;
import com.example.tiferet.polerbear.R;

import java.util.List;

public class Newsfeed extends Activity implements GlobalNewsfeedFragment.GlobalNewsfeedFragmentDelegate{

    GlobalNewsfeedFragment globalNewsfeedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        globalNewsfeedFragment = new GlobalNewsfeedFragment(); //(MyProfileFragment) getFragmentManager().findFragmentById(R.id.profileFragment);
        globalNewsfeedFragment.setDelegate(this);
        //stack.push(myProfileFragment);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.newsfeedContainer, globalNewsfeedFragment);
        ft.commit();
        invalidateOptionsMenu();
    }

    @Override
    public void OnPersonalNewsfeed() {

    }
}
