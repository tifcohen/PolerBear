package com.example.tiferet.polerbear.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.tiferet.polerbear.R;


public class UpdateProgressFragment extends Fragment {
    private static final int REQUEST_TAKE_GALLERY_VIDEO = 666; // the number doesn't matter

    public interface UpdateProgressFragmentDelegate {
        //   void onUpdateProgress();
    }

    ImageView trickImage;
    VideoView videoView;
    UpdateProgressFragment delegate;

    public void setDelegate(UpdateProgressFragment delegate) {
        this.delegate = delegate;
    }

    public UpdateProgressFragment() {
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
        View view = inflater.inflate(R.layout.fragment_update_progress, container, false);

        Button upload = (Button) view.findViewById(R.id.uploadBtn);
        trickImage = (ImageView) view.findViewById(R.id.uploadedImage);

        videoView = (VideoView) view.findViewById(R.id.videoView);

        trickImage.setVisibility(View.GONE);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // only gallery, photo / video

                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Video"), REQUEST_TAKE_GALLERY_VIDEO);

            }
        });


        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
                Uri selectedImageUri = data.getData();

                // OI FILE Manager
                //String filemanagerstring = selectedImageUri.getPath();

                // MEDIA GALLERY
                //String selectedImagePath = getPath(selectedImageUri);
                //if (selectedImagePath != null) {

                    MediaController mediaController= new MediaController(UpdateProgressFragment.this.getActivity());
                    mediaController.setAnchorView(videoView);
                    //Uri uri=Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.one);
                    videoView.setMediaController(mediaController);
                    videoView.setVideoURI(selectedImageUri);
                    videoView.requestFocus();

                    videoView.start();

                //}
            }
        }
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}

