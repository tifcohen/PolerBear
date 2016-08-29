package com.example.tiferet.polerbear.Activities;

import android.media.MediaPlayer;
import android.net.Uri;
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
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.tiferet.polerbear.API.ITricksAPI;
import com.example.tiferet.polerbear.API.IUploadFiles;
import com.example.tiferet.polerbear.R;
import com.example.tiferet.polerbear.Repository.Server.Repository;
import com.example.tiferet.polerbear.Repository.Server.SessionManager;
import com.example.tiferet.polerbear.Repository.Server.TrickForUser;

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
        TextView comment = (TextView) findViewById(R.id.comment);

        spinner.setVisibility(View.VISIBLE);

        session = new SessionManager(getApplicationContext());

        final ITricksAPI api = Repository.getInstance().retrofit.create(ITricksAPI.class);
        final Call<List<TrickForUser>> callTrickForUser = api.getTricksForUser(session.getUserId());

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
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
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
            final ViewHolder viewHolder;
            if (convertView == null) {
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.gallery_single_row, null);
                viewHolder = new ViewHolder();
                viewHolder.trickName = (TextView) convertView.findViewById(R.id.trickName);
                viewHolder.trickDate = (TextView) convertView.findViewById(R.id.trickDate);
                viewHolder.videoview = (VideoView) convertView.findViewById(R.id.trickVideo);
                viewHolder.videoSpinner = (ProgressBar) convertView.findViewById(R.id.videoSpinner);
                viewHolder.comment = (TextView) convertView.findViewById(R.id.comment);
                convertView.setTag(viewHolder);
            }
            else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            TrickForUser trick = tricks.get(position);
            viewHolder.trickName.setText(trick.getTrickName());
            viewHolder.trickDate.setText(trick.getDate());
            viewHolder.comment.setText(trick.getComment());

            final IUploadFiles api = Repository.getInstance().retrofit.create(IUploadFiles.class);
            final Call<String> callVideo = api.getFile(trick.getTrickPic());
            callVideo.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    final String VideoURL = response.body();
                    if (VideoURL == null) {
                        viewHolder.videoview.setVisibility(View.GONE);
                        viewHolder.videoSpinner.setVisibility(View.GONE);
                        return;
                    }
                    viewHolder.videoview.setVisibility(View.VISIBLE);
                    try {
                        viewHolder.videoSpinner.setVisibility(View.VISIBLE);
                        // Start the MediaController
                        MediaController mediacontroller = new MediaController(Gallery.this);
                        mediacontroller.setAnchorView(viewHolder.videoview);
                        // Get the URL from String VideoURL

                        Uri video = Uri.parse(VideoURL);
                        viewHolder.videoview.setMediaController(mediacontroller);
                        viewHolder.videoview.setVideoURI(video);

                    } catch (Exception e) {
                        Log.e("Error", e.getMessage());
                        e.printStackTrace();
                    }

                    viewHolder.videoview.requestFocus();
                    viewHolder.videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        // Close the progress bar and play the video
                        public void onPrepared(MediaPlayer mp) {
                            //Glide.with(getActivity().getApplicationContext()).load(Uri.fromFile(new File(VideoURL))).into(thumbnail);//TODO
                            viewHolder.videoview.pause();
                            viewHolder.videoSpinner.setVisibility(View.GONE);
                        }
                    });
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d("show video", t.getMessage());
                }
            });
            return convertView;
        }
    }

    static class ViewHolder {
        TextView trickName;
        TextView trickDate;
        TextView comment;
        ProgressBar videoSpinner;
        VideoView videoview;
    }
}
