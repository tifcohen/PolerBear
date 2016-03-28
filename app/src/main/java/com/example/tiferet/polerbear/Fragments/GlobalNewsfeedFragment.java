package com.example.tiferet.polerbear.Fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
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

public class GlobalNewsfeedFragment extends Fragment {
    public interface GlobalNewsfeedFragmentDelegate{
        void OnPersonalNewsfeed();
    }

    GlobalNewsfeedFragmentDelegate delegate;
    public void setDelegate(GlobalNewsfeedFragmentDelegate delegate){ this.delegate = delegate;}

    List<User> users;
    ProgressBar spinner;
    ListView usersList;

    // TODO: Rename and change types of parameters
    //private String mParam1;
    //private String mParam2;

    //private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types and number of parameters
    /*public static GlobalNewsfeedFragment newInstance(String param1, String param2) {
        GlobalNewsfeedFragment fragment = new GlobalNewsfeedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
*/
    public GlobalNewsfeedFragment() {
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
        View view = inflater.inflate(R.layout.fragment_global_newsfeed, container, false);
        usersList = (ListView) view.findViewById(R.id.newsFeedList);
        spinner = (ProgressBar) view.findViewById(R.id.spinner);

        users = UserDB.getInstance().getAllUsers();
        //spinner.setVisibility(View.VISIBLE);
        NewsfeedAdapter adapter = new NewsfeedAdapter();
        usersList.setAdapter(adapter);
        spinner.setVisibility(View.GONE);

        return view;
    }
/*
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
*/
    /*
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/
/*
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
*/
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */ /*
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }*/

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
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.newsfeed_single_row, null);
            }
            final TextView userName = (TextView) convertView.findViewById(R.id.otherUsername);
            /*final TextView bookName = (TextView) convertView.findViewById(R.id.bookName);
            final TextView bookReview = (TextView) convertView.findViewById(R.id.bookReview);
            final ImageView stars = (ImageView) convertView.findViewById(R.id.stars);
            final TextView page = (TextView) convertView.findViewById(R.id.pageTextView);
            final TextView action = (TextView) convertView.findViewById(R.id.actionTextView);
            final TextView action2 = (TextView) convertView.findViewById(R.id.action2TextView);
            final ImageView userProfileImage = (ImageView) convertView.findViewById(R.id.userProfileImage);*/

            User user = users.get(position);
            userName.setText(user.getUserName());

            return convertView;
        }
    }
}


