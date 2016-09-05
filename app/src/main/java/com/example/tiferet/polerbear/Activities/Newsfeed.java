package com.example.tiferet.polerbear.Activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.tiferet.polerbear.API.ITricksAPI;
import com.example.tiferet.polerbear.API.IUploadFiles;
import com.example.tiferet.polerbear.API.IUserAPI;
import com.example.tiferet.polerbear.R;
import com.example.tiferet.polerbear.Repository.Server.Repository;
import com.example.tiferet.polerbear.Repository.Server.SessionManager;
import com.example.tiferet.polerbear.Repository.Server.Trick;
import com.example.tiferet.polerbear.Repository.Server.TrickForUser;
import com.example.tiferet.polerbear.Repository.Server.User;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Newsfeed extends AppCompatActivity{

    List<TrickForUser> trickForUsers;
    ProgressBar spinner;
    ListView usersList;
    String currentFeed;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        currentFeed = "Global";

        usersList = (ListView) findViewById(R.id.newsFeedList);
        spinner = (ProgressBar) findViewById(R.id.spinner);
        switchFeed();
    }

    public void onClickUsername(View v) {
        TrickForUser user = (TrickForUser) v.getTag();
        Intent intent = new Intent(getApplicationContext(), MyProfile.class);
        intent.putExtra("ref", user.getUserId().toString());
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), MyProfile.class);
                startActivity(intent);
                finish();
                break;
            case R.id.replace_newsfeed:
                switch (currentFeed){
                    case "Global":
                        currentFeed = "Personal";
                        break;
                    case "Personal":
                        currentFeed = "Global";
                        break;
                }
                switchFeed();
                break;
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem switchFeed = menu.findItem(R.id.replace_newsfeed);
        switchFeed.setIcon(R.drawable.replace_feed);
        switchFeed.setVisible(true);
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_newsfeed, menu);
        return true;
    }

    private void switchFeed(){
        spinner.setVisibility(View.VISIBLE);

        Toast.makeText(getApplicationContext(), currentFeed, Toast.LENGTH_SHORT).show();
        session = new SessionManager(getApplicationContext());

        ITricksAPI apiTrick = Repository.getInstance().retrofit.create(ITricksAPI.class);
        Call<List<TrickForUser>> trickCall;

        if(currentFeed.equals("Personal")){
            trickCall = apiTrick.getAllTricksForFollowingUsers(session.getUserId());
        }
        else{
            trickCall = apiTrick.getAllTricksForUsers();
        }

        trickCall.enqueue(new Callback<List<TrickForUser>>() {
            @Override
            public void onResponse(Call<List<TrickForUser>> call, Response<List<TrickForUser>> response) {
                trickForUsers = response.body();
                NewsfeedAdapter adapter = new NewsfeedAdapter();
                usersList.setAdapter(adapter);
                spinner.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<TrickForUser>> call, Throwable t) {

            }
        });
    }




    class NewsfeedAdapter extends BaseAdapter {

        public NewsfeedAdapter() {
        }

        @Override
        public int getCount() { //returns the size of the list
            return trickForUsers.size();
        }

        @Override
        public Object getItem(int position) { //returns the post
            return trickForUsers.get(position);
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
                convertView = inflater.inflate(R.layout.global_newsfeed_single_row, null);
                viewHolder = new ViewHolder();
                viewHolder.userName = (TextView) convertView.findViewById(R.id.otherUsername);
                viewHolder.trickName = (TextView) convertView.findViewById(R.id.trickname);
                viewHolder.trickLevel = (TextView) convertView.findViewById(R.id.level);
                viewHolder.teachMe = (Button) convertView.findViewById(R.id.teachMeBtn);
                viewHolder.videoSpinner = (ProgressBar) convertView.findViewById(R.id.videoSpinner);
                viewHolder.videoview = (VideoView) convertView.findViewById(R.id.trickVideo);
                viewHolder.userProfileImage = (ImageView) convertView.findViewById(R.id.profilePic);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            final TrickForUser trickForUser = trickForUsers.get(position);
            viewHolder.userName.setText(trickForUser.getUserName());
            viewHolder.trickName.setText(trickForUser.getTrickName());
            viewHolder.videoSpinner.setVisibility(View.VISIBLE);

            viewHolder.userName.setTag(trickForUser);

            if(trickForUser.getUserId()==session.getUserId()){
                viewHolder.teachMe.setVisibility(View.GONE);
            }

            ITricksAPI trickAPI = Repository.getInstance().retrofit.create(ITricksAPI.class);
            Call<Trick> callLevel = trickAPI.getTrick(trickForUser.getTrickId());
            callLevel.enqueue(new Callback<Trick>() {
                @Override
                public void onResponse(Call<Trick> call, Response<Trick> response) {
                    Trick trick = response.body();
                    viewHolder.trickLevel.setText("Level: "+trick.getTrickLevel().toString());
                }

                @Override
                public void onFailure(Call<Trick> call, Throwable t) {

                }
            });

            final IUploadFiles api = Repository.getInstance().retrofit.create(IUploadFiles.class);
            final Call<String> callVideo = api.getFile(trickForUser.getTrickPic());
            callVideo.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    final String VideoURL = response.body();
                    if(VideoURL==null){
                        viewHolder.videoview.setVisibility(View.GONE);
                        viewHolder.videoSpinner.setVisibility(View.GONE);
                        return;
                    }
                    viewHolder.videoview.setVisibility(View.VISIBLE);
                    try {
                        viewHolder.videoSpinner.setVisibility(View.VISIBLE);
                        // Start the MediaController
                        MediaController mediacontroller = new MediaController(Newsfeed.this);
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

            final IUserAPI apiUser = Repository.getInstance().retrofit.create(IUserAPI.class);
            Call<String> picRefCall = apiUser.getUserProfilePicName(trickForUser.getUserId().toString());
            picRefCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    final IUploadFiles apiPic = Repository.getInstance().retrofit.create(IUploadFiles.class);
                    Call<String> picCall = apiPic.getFile(response.body());
                    picCall.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.body() == null) {
                                Call<User> userCall = apiUser.getUser(trickForUser.getUserId());
                                userCall.enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(Call<User> call, Response<User> response) {
                                        if (response.body().getUserSex().equals("Female")) {
                                            viewHolder.userProfileImage.setImageDrawable(getResources().getDrawable(R.drawable.female));
                                        } else {
                                            viewHolder.userProfileImage.setImageDrawable(getResources().getDrawable(R.drawable.male));
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<User> call, Throwable t) {

                                    }
                                });
                            } else {
                                UrlImageViewHelper.setUrlDrawable( viewHolder.userProfileImage, response.body());
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.d("Profile Pic", t.getMessage());
                        }
                    });
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d("Profile Pic ref", t.getMessage());
                }
            });

            viewHolder.teachMe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), NewTrick.class);
                    intent.putExtra("ref", "I\'ll do it");
                    intent.putExtra("trickId", trickForUser.getTrickId()+"");
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }

    static class ViewHolder {
        TextView userName;
        TextView trickName;
        TextView trickLevel;
        Button teachMe;
        ProgressBar videoSpinner;
        VideoView videoview;
        ImageView userProfileImage;

    }
}
