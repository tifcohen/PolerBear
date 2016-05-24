package com.example.tiferet.polerbear.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tiferet.polerbear.R;
import com.example.tiferet.polerbear.Repository.Local.Trick;
import com.example.tiferet.polerbear.Repository.Local.TrickDB;
import com.example.tiferet.polerbear.Repository.Server.SessionManager;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.HashMap;
import java.util.List;

public class MyProfile extends AppCompatActivity {

    final Context context = this;
    List<Trick> tricks;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        session = new SessionManager(getApplicationContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();

        TextView username = (TextView) findViewById(R.id.myProfileUsername);
        TextView userLevel = (TextView) findViewById(R.id.myProfileLevelView);

        username.setText(user.get(SessionManager.KEY_NAME));
        userLevel.setText("Level: " +user.get(SessionManager.KEY_LEVEL));

        final ListView trickList = (ListView) findViewById(R.id.trickInProgressList);
        tricks = TrickDB.getInstance().getAllTricks();
        //spinner.setVisibility(View.VISIBLE);
        TricksInProgressAdapter adapter = new TricksInProgressAdapter();
        trickList.setAdapter(adapter);
        //spinner.setVisibility(View.GONE);

        trickList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("TAG", "row selected" + position);
                Trick trick = tricks.get(position);
                Intent intent = new Intent(getApplicationContext(), Progress.class);
                startActivity(intent);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.logo_transparent);

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        ImageView itemIcon1 = new ImageView(this);
        ImageView itemIcon2 = new ImageView(this);
        ImageView itemIcon3 = new ImageView(this);
        ImageView itemIcon4 = new ImageView(this);

        itemIcon1.setImageResource(R.drawable.newsfeed_icon);
        itemIcon2.setImageResource(R.drawable.warmup_icon);
        itemIcon3.setImageResource(R.drawable.gallery_icon);
        itemIcon4.setImageResource(R.drawable.add_new_trick);

        SubActionButton btnNewsfeed = itemBuilder.setContentView(itemIcon1).build();
        SubActionButton btnWarmup = itemBuilder.setContentView(itemIcon2).build();
        SubActionButton btnGallery = itemBuilder.setContentView(itemIcon3).build();
        SubActionButton btnNewTrick = itemBuilder.setContentView(itemIcon4).build();

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(btnNewsfeed).addSubActionView(btnWarmup).addSubActionView(btnGallery).addSubActionView(btnNewTrick)
                .attachTo(fab)
                .build();

        btnWarmup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SingleTrick.class);
                startActivity(intent);
            }
        });

        btnNewsfeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Newsfeed.class);
                startActivity(intent);
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Gallery.class);
                startActivity(intent);
            }
        });

        btnNewTrick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                // set title
                alertDialogBuilder.setTitle("Add a new trick:");
                // set dialog message
                alertDialogBuilder.setMessage("Do you want to do it yourself or let us do it?").setCancelable(false)
                        .setPositiveButton("Do it!", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(getApplicationContext(), NewTrick.class);
                                startActivity(intent);
                                Snackbar.make(v, "Do it!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            }
                        })
                        .setNegativeButton("I'll do it", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(getApplicationContext(), NewTrick.class);
                                startActivity(intent);
                                Snackbar.make(v, "I'll do it", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            }
                        })
                        .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Snackbar.make(v, "Cancel", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                alertDialog.getWindow().setDimAmount(0.5f);
                alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editBtn: {
                Intent intent = new Intent(getApplicationContext(), EditProfile.class);
                startActivity(intent);
                return true;
            }
            case R.id.logoutBtn: {
                session.logoutUser();
                return true;
            }
            default:
                //return super.onOptionsItemSelected(item);
                return true;
        }
    }

    class TricksInProgressAdapter extends BaseAdapter {

        public TricksInProgressAdapter() {
        }

        @Override
        public int getCount() { //returns the size of the list
            return tricks.size();
        }

        @Override
        public Object getItem(int position) { //returns the post
            return tricks.get(position);
        }

        @Override
        public long getItemId(int position) { //returns post id
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.my_progress_single_trick, null);
            }
            final TextView trickName = (TextView) convertView.findViewById(R.id.trickName);
            final TextView sinceDate = (TextView) convertView.findViewById(R.id.sinceDate);
            /*final TextView bookReview = (TextView) convertView.findViewById(R.id.bookReview);
            final ImageView stars = (ImageView) convertView.findViewById(R.id.stars);
            final TextView page = (TextView) convertView.findViewById(R.id.pageTextView);
            final TextView action = (TextView) convertView.findViewById(R.id.actionTextView);
            final TextView action2 = (TextView) convertView.findViewById(R.id.action2TextView);
            final ImageView userProfileImage = (ImageView) convertView.findViewById(R.id.userProfileImage);*/

            Trick trick = tricks.get(position);
            trickName.setText(trick.getTrickName());

            return convertView;
        }
    }
}