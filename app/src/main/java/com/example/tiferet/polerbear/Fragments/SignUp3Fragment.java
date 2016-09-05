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
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tiferet.polerbear.API.ITricksAPI;
import com.example.tiferet.polerbear.R;
import com.example.tiferet.polerbear.Repository.Server.Repository;
import com.example.tiferet.polerbear.Repository.Server.SessionManager;
import com.example.tiferet.polerbear.Repository.Server.Trick;
import com.example.tiferet.polerbear.Repository.Server.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    ArrayList<Trick> doneBefore;
    String[] levelPicker = new String[]{"Select", "1", "2", "3", "4"};
    SessionManager session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_sign_up3, container, false);

        session = new SessionManager(getActivity().getApplicationContext());

        Button nextBtn = (Button) view.findViewById(R.id.nextBtn);
        final Button cancelBtn = (Button) view.findViewById(R.id.cancelBtn);
        final Spinner levelDropdown = (Spinner) view.findViewById(R.id.levelDropdown);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progBar);
        final ArrayAdapter<String> levelAdapter = new ArrayAdapter<String>(getActivity(), R.layout.dropdown_item_selected, levelPicker);
        //levelDropdown.setAdapter(levelAdapter);
        final ListView trickList = (ListView) view.findViewById(R.id.trickListForRegistration);
        doneBefore = new ArrayList<Trick>();

        trickList.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        //adapter = ArrayAdapter.createFromResource(this, R.array.chunks, android.R.layout.simple_spinner_item);
        levelAdapter.setDropDownViewResource(R.layout.dropdown_items);
        levelDropdown.setAdapter(levelAdapter);
        //spinnerSize.setOnItemSelectedListener(new MyOnItemSelectedListener());
/*
        ITricksAPI apiTrick = Repository.getInstance().retrofit.create(ITricksAPI.class);
        Call<List<Trick>> trickCall = apiTrick.checkLevel(2);
        trickCall.enqueue(new Callback<List<Trick>>() {
            @Override
            public void onResponse(Call<List<Trick>> call, Response<List<Trick>> response) {
                trickList.setVisibility(View.VISIBLE);
                tricks = response.body();
                LevelTricksAdapter adapter = new LevelTricksAdapter();
                trickList.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Trick>> call, Throwable t) {
                Log.d("trickList", t.getMessage());
            }
        });
*/
        final ITricksAPI apiTrick = Repository.getInstance().retrofit.create(ITricksAPI.class);
        levelDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                progressBar.setVisibility(View.VISIBLE);
                Call<List<Trick>> trickCall = apiTrick.checkLevel(position);
                trickCall.enqueue(new Callback<List<Trick>>() {
                    @Override
                    public void onResponse(Call<List<Trick>> call, Response<List<Trick>> response) {
                        progressBar.setVisibility(View.GONE);
                        trickList.setVisibility(View.VISIBLE);
                        tricks = response.body();
                        LevelTricksAdapter adapter = new LevelTricksAdapter();
                        trickList.setAdapter(adapter);
                    }
                    @Override
                    public void onFailure(Call<List<Trick>> call, Throwable t) {

                    }
                });
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
                progressBar.setVisibility(View.VISIBLE);
                Call<Integer> trickCall = apiTrick.calLevel(user.getUserId(),doneBefore);
                trickCall.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        progressBar.setVisibility(View.GONE);
                        user.setUserLevel(response.body());
                        session.updateLevelSession(Integer.parseInt(response.body()+""));
                        if (delegate != null) {
                            delegate.OnSignUp4(user);
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {

                    }
                });
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
            final CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkbox);
            final Trick trick = tricks.get(position);
            trickName.setText(trick.getTrickName());
            cb.setTag(position);
            cb.setChecked(doneBefore.contains(tricks.get(position)));

            cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cb.isChecked()) {
                        doneBefore.add(trick);
                    } else {
                        doneBefore.remove(trick);
                    }
                }
            });

            return convertView;
        }
    }
}

