package com.example.tiferet.polerbear.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tiferet.polerbear.R;
import com.example.tiferet.polerbear.Repository.Local.Trick;
import com.example.tiferet.polerbear.Repository.Local.TrickDB;
import com.example.tiferet.polerbear.Repository.Server.User;

import java.util.List;

public class SignUp3Fragment extends Fragment {
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public interface SignUp3FragmentDelegate{
        void OnSignUp4(User user);
        void OnCancel();
    }

    SignUp3FragmentDelegate delegate;
    public void setDelegate(SignUp3FragmentDelegate delegate){this.delegate=delegate;}

    public SignUp3Fragment() {
        // Required empty public constructor
    }

    List<Trick> tricks;
    String[] levelPicker = new String[]{"1", "2", "3", "4", "5"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_sign_up3, container, false);

        Button nextBtn = (Button) view.findViewById(R.id.nextBtn);
        Button cancelBtn = (Button) view.findViewById(R.id.cancelBtn);
        final Spinner levelDropdown = (Spinner) view.findViewById(R.id.levelDropdown);
        final ArrayAdapter<String> levelAdapter = new ArrayAdapter<String>(getActivity(), R.layout.dropdown_item_selected, levelPicker);
        //levelDropdown.setAdapter(levelAdapter);
        final ListView trickList = (ListView) view.findViewById(R.id.trickListForRegistration);

        //adapter = ArrayAdapter.createFromResource(this, R.array.chunks, android.R.layout.simple_spinner_item);
        levelAdapter.setDropDownViewResource(R.layout.dropdown_items);
        levelDropdown.setAdapter(levelAdapter);
        //spinnerSize.setOnItemSelectedListener(new MyOnItemSelectedListener());

        levelDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tricks = TrickDB.getInstance().getAllTricks();
                //spinner.setVisibility(View.VISIBLE);
                LevelTricksAdapter adapter = new LevelTricksAdapter();
                trickList.setAdapter(adapter);
                //spinner.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                trickList.setVisibility(view.INVISIBLE);
                Toast.makeText(getActivity().getApplicationContext(), "please pick a level", Toast.LENGTH_LONG).show();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delegate != null) {
                    delegate.OnSignUp4(user);
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delegate.OnCancel();
            }
        });
        return view;
    }

    class LevelTricksAdapter extends BaseAdapter {

        public LevelTricksAdapter() {
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
                convertView = inflater.inflate(R.layout.calculate_level_single_trick, null);
            }
            final TextView trickName = (TextView) convertView.findViewById(R.id.trickName);
            /*final TextView bookName = (TextView) convertView.findViewById(R.id.bookName);
            final TextView bookReview = (TextView) convertView.findViewById(R.id.bookReview);
            final ImageView stars = (ImageView) convertView.findViewById(R.id.stars);
            final TextView page = (TextView) convertView.findViewById(R.id.pageTextView);
            final TextView action = (TextView) convertView.findViewById(R.id.actionTextView);
            final TextView action2 = (TextView) convertView.findViewById(R.id.action2TextView);
            final ImageView userProfileImage = (ImageView) convertView.findViewById(R.id.userProfileImage);*/

            Trick trick = tricks.get(position);
            trickName.setText(trick.getTrickName());

            return convertView;
        }
    }
}

