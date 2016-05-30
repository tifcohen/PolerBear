package com.example.tiferet.polerbear.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tiferet.polerbear.API.ITricksAPI;
import com.example.tiferet.polerbear.R;
import com.example.tiferet.polerbear.Repository.Server.Repository;
import com.example.tiferet.polerbear.Repository.Server.SessionManager;
import com.example.tiferet.polerbear.Repository.Server.Trick;
import com.example.tiferet.polerbear.Repository.Server.TrickForUser;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProgressFragment extends Fragment {
    public interface ProgressFragmentDelegate {
        void OnUpdateProgress();
    }

    public void setTrickId(String trickId){this.trickId=trickId;}

    String trickId;
    SessionManager session;
    List<TrickForUser> tricks;
    ListView trickList;
    ProgressFragmentDelegate delegate;
    public void setDelegate(ProgressFragmentDelegate delegate){this.delegate = delegate;}

    public ProgressFragment() {
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
        View view = inflater.inflate(R.layout.fragment_progress, container, false);

        session = new SessionManager(getActivity().getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();

        trickList = (ListView) view.findViewById(R.id.trickList);
        final TextView trickName = (TextView) view.findViewById(R.id.trickName);
        final TextView trickLevel = (TextView) view.findViewById(R.id.trickLevel);
        final TextView sinceDate = (TextView) view.findViewById(R.id.sinceDate);

        final ITricksAPI api = Repository.getInstance().retrofit.create(ITricksAPI.class);

        Call<Trick> callTrick = api.getTrick(Integer.parseInt(trickId));
        callTrick.enqueue(new Callback<Trick>() {
            @Override
            public void onResponse(Call<Trick> call, Response<Trick> response) {
                trickName.setText(response.body().getTrickName());
                trickLevel.setText("Level "+response.body().getTrickLevel());
            }

            @Override
            public void onFailure(Call<Trick> call, Throwable t) {

            }
        });

        final Call<List<TrickForUser>> callTrickForUser = api.getTrickInProgress(Integer.parseInt(user.get(SessionManager.KEY_ID)),Integer.parseInt(trickId));
        callTrickForUser.enqueue(new Callback<List<TrickForUser>>() {
            @Override
            public void onResponse(Call<List<TrickForUser>> call, Response<List<TrickForUser>> response) {
                tricks = response.body();
                ProgressAdapter adapter = new ProgressAdapter();
                trickList.setAdapter(adapter);

                Collections.sort(tricks, new Comparator<TrickForUser>() {
                    public int compare(TrickForUser t1, TrickForUser t2) {
                        return t1.getDate().compareTo(t2.getDate());
                    }
                });
                sinceDate.setText("Started on: "+tricks.get(0).getDate());
            }

            @Override
            public void onFailure(Call<List<TrickForUser>> call, Throwable t) {

            }
        });
        Log.d("TAG", trickId);
        //TextView trickName = (TextView) view.findViewById(R.id.trickName);
        //trickName.setText(thisTrick.getTrickName());

        return view;
    }

    class ProgressAdapter extends BaseAdapter {

        public ProgressAdapter() {
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
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.trick_progress_single_row, null);
            }

            final TextView trickDate = (TextView) convertView.findViewById(R.id.progressDate);
            final TextView comment = (TextView) convertView.findViewById(R.id.trickProgressComments);
            /*final ImageView stars = (ImageView) convertView.findViewById(R.id.stars);
            final TextView page = (TextView) convertView.findViewById(R.id.pageTextView);
            final TextView action = (TextView) convertView.findViewById(R.id.actionTextView);
            final TextView action2 = (TextView) convertView.findViewById(R.id.action2TextView);
            final ImageView userProfileImage = (ImageView) convertView.findViewById(R.id.userProfileImage);*/

            TrickForUser trick = tricks.get(position);
            trickDate.setText(trick.getDate());
            comment.setText(trick.getComment());

            return convertView;
        }
    }
}
