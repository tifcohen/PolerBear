package com.example.tiferet.polerbear.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.tiferet.polerbear.R;


public class UpdateProgressFragment extends Fragment {
    public interface UpdateProgressFragmentDelegate{
     //   void onUpdateProgress();
    }

    UpdateProgressFragment delegate;
    public void setDelegate(UpdateProgressFragment delegate){ this.delegate = delegate;}

    public UpdateProgressFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_progress, container, false);

        Button upload = (Button) view.findViewById(R.id.uploadBtn);
        ImageView image = (ImageView) view.findViewById(R.id.uploadedImage);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // only gallery, photo / video
            }
        });


        return view;
    }

}
