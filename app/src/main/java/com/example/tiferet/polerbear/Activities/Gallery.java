package com.example.tiferet.polerbear.Activities;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tiferet.polerbear.API.ITricksAPI;
import com.example.tiferet.polerbear.R;
import com.example.tiferet.polerbear.Repository.Server.Repository;
import com.example.tiferet.polerbear.Repository.Server.SessionManager;
import com.example.tiferet.polerbear.Repository.Server.TrickForUser;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Gallery extends AppCompatActivity {

    String trickId;
    SessionManager session;
    List<TrickForUser> tricks;
    ListView trickList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        final ProgressBar spinner = (ProgressBar) findViewById(R.id.spinner);

        spinner.setVisibility(View.VISIBLE);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();

        final ITricksAPI api = Repository.getInstance().retrofit.create(ITricksAPI.class);
        final Call<List<TrickForUser>> callTrickForUser = api.getTricksForUser(Integer.parseInt(user.get(SessionManager.KEY_ID)));

        callTrickForUser.enqueue(new Callback<List<TrickForUser>>() {
            @Override
            public void onResponse(Call<List<TrickForUser>> call, Response<List<TrickForUser>> response) {
                tricks = response.body();
                GalleryAdapter adapter = new GalleryAdapter();
                trickList.setAdapter(adapter);
                spinner.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<TrickForUser>> call, Throwable t) {
                Log.d("TAG", "failed to fetch tricks");
            }
        });
        trickList = (ListView) findViewById(R.id.galleryList);

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

    class GalleryAdapter extends BaseAdapter {

        public GalleryAdapter() {
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
                convertView = inflater.inflate(R.layout.gallery_single_row, null);
            }
            final TextView trickName = (TextView) convertView.findViewById(R.id.trickName);
            final TextView trickDate = (TextView) convertView.findViewById(R.id.trickDate);
            final TextView comment = (TextView) convertView.findViewById(R.id.comment);

            TrickForUser trick = tricks.get(position);
            trickName.setText(trick.getTrickName());
            trickDate.setText(trick.getDate());
            comment.setText(trick.getComment());

            return convertView;
        }
    }
}
