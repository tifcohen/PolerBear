package com.example.tiferet.polerbear.Activities;

import android.app.Activity;
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
import com.example.tiferet.polerbear.R;

import java.util.List;

public class Newsfeed extends Activity {

    List<User> users;
    ProgressBar spinner;
    ListView usersList;

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

        usersList = (ListView) findViewById(R.id.newsFeedList);
        spinner = (ProgressBar) findViewById(R.id.spinner);
        users = UserDB.getInstance().getAllUsers();
        //spinner.setVisibility(View.VISIBLE);
        NewsfeedAdapter adapter = new NewsfeedAdapter();
        usersList.setAdapter(adapter);
        spinner.setVisibility(View.GONE);

    }

    class NewsfeedAdapter extends BaseAdapter {

        public NewsfeedAdapter() {
        }

        @Override
        public int getCount() { //returns the size of the list
            return users.size();
        }

        @Override
        public Object getItem(int position) { //returns the post
            return users.get(position);
        }

        @Override
        public long getItemId(int position) { //returns post id
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.newsfeed_single_row, null);
            }
            final TextView userName = (TextView) convertView.findViewById(R.id.userProfileName);
            final TextView bookName = (TextView) convertView.findViewById(R.id.bookName);
            final TextView bookReview = (TextView) convertView.findViewById(R.id.bookReview);
            final ImageView stars = (ImageView) convertView.findViewById(R.id.stars);
            final TextView page = (TextView) convertView.findViewById(R.id.pageTextView);
            final TextView action = (TextView) convertView.findViewById(R.id.actionTextView);
            final TextView action2 = (TextView) convertView.findViewById(R.id.action2TextView);
            final ImageView userProfileImage = (ImageView) convertView.findViewById(R.id.userProfileImage);

            User user = users.get(position);
            userName.setText(user.getUserName());

            return convertView;
        }
    }

}
