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

import com.example.tiferet.polerbear.R;
import com.example.tiferet.polerbear.Repository.Local.Trick;
import com.example.tiferet.polerbear.Repository.Local.TrickDB;

import java.util.List;

public class ProgressFragment extends Fragment {
    public interface ProgressFragmentDelegate {
        void OnUpdateProgress();
    }

    public void setTrickId(String trickId){this.trickId=trickId;}

    String trickId;
    Trick thisTrick;
    List<Trick> tricks;
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

        trickList = (ListView) view.findViewById(R.id.trickList);
        tricks = TrickDB.getInstance().getAllTricks();
        ProgressAdapter adapter = new ProgressAdapter();
        trickList.setAdapter(adapter);

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
            /*final TextView bookReview = (TextView) convertView.findViewById(R.id.bookReview);
            final ImageView stars = (ImageView) convertView.findViewById(R.id.stars);
            final TextView page = (TextView) convertView.findViewById(R.id.pageTextView);
            final TextView action = (TextView) convertView.findViewById(R.id.actionTextView);
            final TextView action2 = (TextView) convertView.findViewById(R.id.action2TextView);
            final ImageView userProfileImage = (ImageView) convertView.findViewById(R.id.userProfileImage);*/

            Trick trick = tricks.get(position);
            trickDate.setText("date");


            return convertView;
        }
    }
}
