package com.example.tiferet.polerbear.Activities;

import android.app.ProgressDialog;
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
import android.widget.VideoView;

import com.example.tiferet.polerbear.API.ITricksAPI;
import com.example.tiferet.polerbear.API.IUploadFiles;
import com.example.tiferet.polerbear.R;
import com.example.tiferet.polerbear.Repository.Server.Repository;
import com.example.tiferet.polerbear.Repository.Server.SessionManager;
import com.example.tiferet.polerbear.Repository.Server.Trick;

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
        Button addBtn = (Button) findViewById(R.id.addProgress);
        Button tryAgainBtn = (Button) findViewById(R.id.roleAnother);


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
                    Trick trick = response.body();
                    trickName.setText(trick.getTrickName());
                    trickLevel.setText("Level: "+trick.getTrickLevel());
                    playVideo("40_9_9");
                    //playVideo(trick.getTrickVideoName());
                }

                @Override
                public void onFailure(Call<Trick> call, Throwable t) {

                }
            });
        }

        else if(ref.equals("I'll do it")){
            /*
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

            try {
                // Start the MediaController
                MediaController mediacontroller = new MediaController(
                        NewTrick.this);
                mediacontroller.setAnchorView(videoview);
                // Get the URL from String VideoURL
                Uri video = Uri.parse(VideoURL);
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
            });*/
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

                try {
                    // Start the MediaController
                    MediaController mediacontroller = new MediaController(
                            NewTrick.this);
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
