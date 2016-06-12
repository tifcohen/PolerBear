package com.example.tiferet.polerbear.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.tiferet.polerbear.API.ITricksAPI;
import com.example.tiferet.polerbear.API.IUploadFiles;
import com.example.tiferet.polerbear.Activities.MyProfile;
import com.example.tiferet.polerbear.R;
import com.example.tiferet.polerbear.Repository.Local.User;
import com.example.tiferet.polerbear.Repository.Server.Repository;
import com.example.tiferet.polerbear.Repository.Server.SessionManager;
import com.example.tiferet.polerbear.Repository.Server.TrickForUser;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalNewsfeedFragment extends Fragment {
    public interface PersonalNewsfeedFragmentDelegate{
        void OnGlobalNewsfeed();
    }

    PersonalNewsfeedFragmentDelegate delegate;
    public void setDelegate(PersonalNewsfeedFragmentDelegate delegate){ this.delegate = delegate;}

    List<TrickForUser> trickForUsers;
    ProgressBar spinner;
    ListView usersList;
    SessionManager session;

    public PersonalNewsfeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal_newsfeed, container, false);
        usersList = (ListView) view.findViewById(R.id.newsFeedList);
        spinner = (ProgressBar) view.findViewById(R.id.spinner);
        final TextView message = (TextView) view.findViewById(R.id.message);
        session = new SessionManager(getActivity().getApplicationContext());
        final HashMap<String, String> user = session.getUserDetails();

        Toast.makeText(getActivity().getApplicationContext(),"Personal", Toast.LENGTH_LONG).show();
        message.setVisibility(View.GONE);
        ITricksAPI apiTrick = Repository.getInstance().retrofit.create(ITricksAPI.class);
        Call<List<TrickForUser>> trickCall = apiTrick.getAllTricksForFollowingUsers(Integer.parseInt(user.get(SessionManager.KEY_ID))); //TODO: String or int?

        trickCall.enqueue(new Callback<List<TrickForUser>>() {
            @Override
            public void onResponse(Call<List<TrickForUser>> call, Response<List<TrickForUser>> response) {
                trickForUsers = response.body();
                if(trickForUsers!=null) {
                    NewsfeedAdapter adapter = new NewsfeedAdapter();
                    usersList.setAdapter(adapter);
                }
                else{
                    message.setVisibility(View.VISIBLE);
                }
                spinner.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<TrickForUser>> call, Throwable t) {

            }
        });

        /*
        users = UserDB.getInstance().getAllUsers();
        spinner.setVisibility(View.VISIBLE);
        NewsfeedAdapter adapter = new NewsfeedAdapter();
        usersList.setAdapter(adapter);
        spinner.setVisibility(View.GONE);*/

        return view;
    }

    public void onClickUsername(View v){
        User user = (User) v.getTag();
        Intent intent = new Intent(getActivity().getApplicationContext(), MyProfile.class);
        intent.putExtra("fragment", "user");
        intent.putExtra("userId", user.getUserId());
        startActivity(intent);
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
            if (convertView == null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.global_newsfeed_single_row, null);
            }

            final TextView userName = (TextView) convertView.findViewById(R.id.otherUsername);
            final TextView trickName = (TextView) convertView.findViewById(R.id.trickname);
            final TextView trickLevel = (TextView) convertView.findViewById(R.id.level);
            final Button teachMe = (Button) convertView.findViewById(R.id.teachMeBtn);
            final ProgressBar videoSpinner = (ProgressBar) convertView.findViewById(R.id.videoSpinner);
            final VideoView videoview = (VideoView) convertView.findViewById(R.id.trickVideo);

            TrickForUser trickForUser = trickForUsers.get(position);
            userName.setText(trickForUser.getUserName());
            trickName.setText(trickForUser.getTrickName());
            trickLevel.setText(""); //TODO

            videoSpinner.setVisibility(View.VISIBLE);

            final IUploadFiles api = Repository.getInstance().retrofit.create(IUploadFiles.class);
            final Call<String> callVideo = api.getFile(trickForUser.getTrickPic());
            callVideo.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    final String VideoURL = response.body();
                    try {
                        videoSpinner.setVisibility(View.GONE);
                        // Start the MediaController
                        MediaController mediacontroller = new MediaController(getActivity());
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
                            //Glide.with(getActivity().getApplicationContext()).load(Uri.fromFile(new File(VideoURL))).into(thumbnail);//TODO
                            videoview.pause();
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
}


