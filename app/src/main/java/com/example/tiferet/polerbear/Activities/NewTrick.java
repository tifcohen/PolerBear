package com.example.tiferet.polerbear.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.tiferet.polerbear.API.ITricksAPI;
import com.example.tiferet.polerbear.API.IUploadFiles;
import com.example.tiferet.polerbear.R;
import com.example.tiferet.polerbear.Repository.Server.Repository;
import com.example.tiferet.polerbear.Repository.Server.SessionManager;
import com.example.tiferet.polerbear.Repository.Server.Trick;
import com.example.tiferet.polerbear.Repository.Server.TrickForUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewTrick extends AppCompatActivity {

    // Declare variables
    ProgressDialog pDialog;
    VideoView videoview;

    // Insert your Video URL
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trick);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final String VideoURL;
        session = new SessionManager(getApplicationContext());
        session.checkLogin();
        final HashMap<String, String> user = session.getUserDetails();

        final TextView trickLevel = (TextView) findViewById(R.id.trickLevel);
        final TextView trickName = (TextView) findViewById(R.id.trickName);
        videoview = (VideoView) findViewById(R.id.trickVideo);
        TextView message = (TextView) findViewById(R.id.message);
        Button backBtn = (Button) findViewById(R.id.backBtn);
        final Button addBtn = (Button) findViewById(R.id.addProgress);
        Button tryAgainBtn = (Button) findViewById(R.id.roleAnother);
        //TextureVideoView textureVideoView = findViewById(R.d.)

        String ref = getIntent().getStringExtra("ref");
        if(ref.equals("warmup")){
            videoview.setVisibility(View.GONE);
            trickLevel.setVisibility(View.GONE);
            trickName.setText("Warmup");
            message.setText("Coming soon!");
            message.setVisibility(View.VISIBLE);
            addBtn.setVisibility(View.GONE);
            tryAgainBtn.setVisibility(View.GONE);
        }
        else if(ref.equals("Do it!")){
            ITricksAPI apiTrick = Repository.getInstance().retrofit.create(ITricksAPI.class);
            Call<Trick> trickCall = apiTrick.generateTrick(Integer.parseInt(user.get(SessionManager.KEY_ID)));
            trickCall.enqueue(new Callback<Trick>() {
                @Override
                public void onResponse(Call<Trick> call, Response<Trick> response) {
                    final Trick trick = response.body();
                    trickName.setText(trick.getTrickName());
                    trickLevel.setText("Level: "+trick.getTrickLevel());
                    playVideo(trick.getTrickVideoName());

                    addBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = new Date();
                            TrickForUser trickForUser = new TrickForUser();
                            trickForUser.setUserId(Integer.parseInt(user.get(SessionManager.KEY_ID)));
                            trickForUser.setUserName(user.get(SessionManager.KEY_NAME));
                            trickForUser.setComment("Just started...");
                            trickForUser.setTrickId(trick.getTrickId());
                            trickForUser.setIsFinished(0);
                            trickForUser.setTrickName(trick.getTrickName());
                            trickForUser.setDate(dateFormat.format(date));
                            ITricksAPI trickAPI = Repository.getInstance().retrofit.create(ITricksAPI.class);
                            Call<Void> call = trickAPI.addProgress("application/json", trickForUser);
                            call.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    Toast.makeText(getApplicationContext(), "Successfully added to your tricks list!", Toast.LENGTH_SHORT).show();;
                                    Intent intent = new Intent(getApplicationContext(),MyProfile.class);
                                    intent.putExtra("ref", "");
                                    startActivity(intent);
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {

                                }
                            });
                        }
                    });
                }

                @Override
                public void onFailure(Call<Trick> call, Throwable t) {

                }
            });
        }

        else if(ref.equals("I'll do it")){

        }


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }

    void playVideo(final String videoURL){

        IUploadFiles apiVideo = Repository.getInstance().retrofit.create(IUploadFiles.class);
        Call<String> videoCall = apiVideo.getFile(videoURL);
        videoCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
               // Create a progressbar
                pDialog = new ProgressDialog(NewTrick.this);
                // Set progressbar title
                pDialog.setTitle("Your new trick is on its way!");
                // Set progressbar message
                pDialog.setMessage("Buffering...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                // Show progressbar
                pDialog.show();
                String videoURL = response.body();
                Log.d("play", response.body());
                try {
                    // Start the MediaController
                    MediaController mediacontroller = new MediaController(NewTrick.this);
                    mediacontroller.setAnchorView(videoview);
                    // Get the URL from String VideoURL
                    Uri video = Uri.parse(videoURL);
                    videoview.setMediaController(mediacontroller);
                    videoview.setVideoURI(video);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }

                videoview.requestFocus();
                videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    // Close the progress bar and play the video
                    public void onPrepared(MediaPlayer mp) {
                        pDialog.dismiss();
                        videoview.start();
                    }
                });
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}
