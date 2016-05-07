package com.example.tiferet.polerbear.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tiferet.polerbear.R;
import com.example.tiferet.polerbear.Repository.Local.User;
import com.example.tiferet.polerbear.Repository.Local.UserDB;

import java.util.List;

public class GlobalNewsfeedFragment extends Fragment {
    public interface GlobalNewsfeedFragmentDelegate{
        void OnPersonalNewsfeed();
        void onClickUsername(View v);
    }

    GlobalNewsfeedFragmentDelegate delegate;
    public void setDelegate(GlobalNewsfeedFragmentDelegate delegate){ this.delegate = delegate;}

    List<User> users;
    ProgressBar spinner;
    ListView usersList;

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

        Toast.makeText(getActivity().getApplicationContext(), "Global", Toast.LENGTH_LONG).show();

        users = UserDB.getInstance().getAllUsers();
        spinner.setVisibility(View.VISIBLE);
        NewsfeedAdapter adapter = new NewsfeedAdapter();
        usersList.setAdapter(adapter);
        spinner.setVisibility(View.GONE);

        return view;


    }

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
                convertView = inflater.inflate(R.layout.global_newsfeed_single_row, null);
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


