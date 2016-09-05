package com.example.tiferet.polerbear.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewTrick extends AppCompatActivity {

    // Declare variables
    ProgressDialog pDialog;
    VideoView videoview;
    TextView trickLevel;
    TextView trickName;
    Button addBtn;
    Button tryAnother;
    final Context context = this;

    // Insert your Video URL
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trick);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        final String VideoURL;
        session = new SessionManager(getApplicationContext());

        trickLevel = (TextView) findViewById(R.id.trickLevel);
        trickName = (TextView) findViewById(R.id.trickName);
        videoview = (VideoView) findViewById(R.id.trickVideo);
        TextView message = (TextView) findViewById(R.id.message);
        Button backBtn = (Button) findViewById(R.id.backBtn);
        addBtn = (Button) findViewById(R.id.addProgress);
        tryAnother = (Button) findViewById(R.id.roleAnother);
        //TextureVideoView textureVideoView = findViewById(R.d.)

        String ref = getIntent().getStringExtra("ref");
        if(ref.equals("warmup")){
            getSupportActionBar().setTitle("Warmup");
            videoview.setVisibility(View.VISIBLE);
            trickLevel.setVisibility(View.GONE);
            trickName.setText("It is highly important to warm up before the workout!");
            message.setVisibility(View.GONE);
            addBtn.setVisibility(View.GONE);
            tryAnother.setVisibility(View.GONE);
            ITricksAPI apiTrick = Repository.getInstance().retrofit.create(ITricksAPI.class);
            Call<Trick> warmupCall = apiTrick.getTrick(40);
            warmupCall.enqueue(new Callback<Trick>() {
                @Override
                public void onResponse(Call<Trick> call, Response<Trick> response) {
                    Trick trick = response.body();
                    playVideo(trick.getTrickVideoName());
                }

                @Override
                public void onFailure(Call<Trick> call, Throwable t) {

                }
            });
        }
        else if(ref.equals("Do it!")){
            generate();
            tryAnother.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    generate();
                }
            });
        }

        else if(ref.equals("I'll do it")){
            tryAnother.setVisibility(View.GONE);
            final String trickId = getIntent().getStringExtra("trickId");
            final ITricksAPI apiTrick = Repository.getInstance().retrofit.create(ITricksAPI.class);
            Call<Trick> trickCall = apiTrick.checkLevelGetTrick(session.getUserId(),Integer.parseInt(trickId));
            trickCall.enqueue(new Callback<Trick>() {
                @Override
                public void onResponse(Call<Trick> call, Response<Trick> response) {
                    final Trick trick = response.body();
                    getSupportActionBar().setTitle("New trick: "+trick.getTrickName());
                    trickName.setText(trick.getTrickName());
                    trickLevel.setText("Level: "+trick.getTrickLevel());
                    if (trick.getTrickId()==Integer.parseInt(trickId)){
                        playVideo(trick.getTrickVideoName());
                    }
                    else{
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                        // set title
                        alertDialogBuilder.setTitle("Add a new trick:");
                        // set dialog message
                        alertDialogBuilder.setMessage("The trick you requested is not in the right level for you.\nPlease complete the following trick in order to try the requested one.").setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        playVideo(trick.getTrickVideoName());
                                    }
                                })
                                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        onBackPressed();
                                        finish();
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        alertDialog.getWindow().setDimAmount(0.5f);
                        alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                    }
                }

                @Override
                public void onFailure(Call<Trick> call, Throwable t) {

                }
            });

        }


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }

    void generate(){
        ITricksAPI apiTrick = Repository.getInstance().retrofit.create(ITricksAPI.class);
        Call<Trick> trickCall = apiTrick.generateTrick(session.getUserId());
        trickCall.enqueue(new Callback<Trick>() {
            @Override
            public void onResponse(Call<Trick> call, Response<Trick> response) {
                final Trick trick = response.body();
                getSupportActionBar().setTitle("New trick: "+trick.getTrickName());
                trickName.setText(trick.getTrickName());
                trickLevel.setText("Level: "+trick.getTrickLevel());
                playVideo(trick.getTrickVideoName());

                addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = new Date();
                        TrickForUser trickForUser = new TrickForUser();
                        trickForUser.setUserId(session.getUserId());
                        trickForUser.setUserName(session.getName());
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
                                Toast.makeText(getApplicationContext(), "Successfully added to your tricks list!", Toast.LENGTH_SHORT).show();
                                //Intent intent = new Intent(getApplicationContext(),MyProfile.class);
                                //intent.putExtra("ref", "backFromTrick");
                                //startActivity(intent);
                                onBackPressed();
                                finish();

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return true;
    }
}
