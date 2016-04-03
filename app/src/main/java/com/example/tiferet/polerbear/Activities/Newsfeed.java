package com.example.tiferet.polerbear.Activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tiferet.polerbear.Classes.User;
import com.example.tiferet.polerbear.Classes.UserDB;
import com.example.tiferet.polerbear.Fragments.GlobalNewsfeedFragment;
import com.example.tiferet.polerbear.Fragments.PersonalNewsfeedFragment;
import com.example.tiferet.polerbear.R;

import java.util.List;

public class Newsfeed extends AppCompatActivity implements GlobalNewsfeedFragment.GlobalNewsfeedFragmentDelegate, PersonalNewsfeedFragment.PersonalNewsfeedFragmentDelegate{

    String current;
    GlobalNewsfeedFragment globalNewsfeedFragment;
    PersonalNewsfeedFragment personalNewsfeedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        current = "personal";
        personalNewsfeedFragment = new PersonalNewsfeedFragment();
        personalNewsfeedFragment.setDelegate(this);
        //stack.push(myProfileFragment);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.newsfeedContainer, personalNewsfeedFragment);
        ft.commit();
        invalidateOptionsMenu();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Log.d("TAG", "fab pressed");
                if(current.equals("personal")) {
                    OnGlobalNewsfeed();
                }
                else{
                    OnPersonalNewsfeed();
                }
            }
        });
    }

    @Override
    public void OnPersonalNewsfeed() {
        current = "personal";
        //personalNewsfeedFragment.setDelegate(this);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.newsfeedContainer, personalNewsfeedFragment);
        ft.hide(globalNewsfeedFragment);
        ft.commit();
        invalidateOptionsMenu();
    }

    @Override
    public void OnGlobalNewsfeed() {
        current = "global";
        globalNewsfeedFragment = new GlobalNewsfeedFragment();
        globalNewsfeedFragment.setDelegate(this);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.newsfeedContainer, globalNewsfeedFragment);
        ft.hide(personalNewsfeedFragment);
        ft.commit();
        invalidateOptionsMenu();
    }
}
