package com.example.tiferet.polerbear.Activities;

import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.tiferet.polerbear.R;

public class NewTrick extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trick);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        VideoView trickVid = (VideoView) findViewById(R.id.trickVideo);
        trickVid.setVisibility(View.GONE);
/*
        LinearLayout lt = (LinearLayout) findViewById(R.id.linearLayout);
        for(int i=0; i<10; i++) {
            TextView tv = (TextView) getLayoutInflater().inflate(R.layout.content_new_trick, lt);
            tv.setText(i+"");
            lt.addView(tv);
        }
*/
    }

}
