package com.example.tiferet.polerbear.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.tiferet.polerbear.Picker.Image.ImagePicker;
import com.example.tiferet.polerbear.R;


public class UpdateProgressFragment extends Fragment {
    private static final int PICK_IMAGE_ID = 234; // the number doesn't matter

    public interface UpdateProgressFragmentDelegate{
     //   void onUpdateProgress();
    }

    ImageView trickImage;

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
        trickImage = (ImageView) view.findViewById(R.id.uploadedImage);

        trickImage.setVisibility(View.GONE);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // only gallery, photo / video
                trickImage.setVisibility(View.VISIBLE);
                Intent chooseImageIntent = ImagePicker.getPickImageIntent(getActivity().getApplicationContext());
                startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);

            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case PICK_IMAGE_ID:
                Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), resultCode, data);
                // TODO use bitmap
                trickImage.setImageBitmap(bitmap);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
}

