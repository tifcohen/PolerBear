package com.example.tiferet.polerbear.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tiferet.polerbear.Classes.Trick;
import com.example.tiferet.polerbear.Classes.TrickDB;
import com.example.tiferet.polerbear.R;

import java.util.List;

public class MyProfile extends AppCompatActivity {

    List<Trick> tricks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ListView trickList = (ListView) findViewById(R.id.trickInProgressList);

        tricks = TrickDB.getInstance().getAllTricks();
        //spinner.setVisibility(View.VISIBLE);
        TricksInProgressAdapter adapter = new TricksInProgressAdapter();
        trickList.setAdapter(adapter);
        //spinner.setVisibility(View.GONE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
