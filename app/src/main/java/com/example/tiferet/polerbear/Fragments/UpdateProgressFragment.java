package com.example.tiferet.polerbear.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.tiferet.polerbear.API.IUploadFiles;
import com.example.tiferet.polerbear.R;
import com.example.tiferet.polerbear.Repository.Server.Repository;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UpdateProgressFragment extends Fragment {
    private static final int REQUEST_TAKE_GALLERY_VIDEO = 666; // the number doesn't matter

    private Uri selectedImageUri;

    public interface UpdateProgressFragmentDelegate {
        //   void onUpdateProgress();
    }

    ImageView trickImage;
    VideoView videoView;
    UpdateProgressFragment delegate;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setTrickId(String trickId) {
        this.trickId = trickId;
    }

    String userId;
    String trickId;

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

        Button saveBtn = (Button) view.findViewById(R.id.saveBtn);
        Button cancelBtn = (Button) view.findViewById(R.id.cancelBtn);
        Button upload = (Button) view.findViewById(R.id.uploadBtn);
        trickImage = (ImageView) view.findViewById(R.id.uploadedImage);

        videoView = (VideoView) view.findViewById(R.id.videoView);
        videoView.setVisibility(View.GONE);
        trickImage.setVisibility(View.GONE);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // only gallery, photo / video
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Video"), REQUEST_TAKE_GALLERY_VIDEO);
                videoView.setVisibility(View.VISIBLE);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selectedImageUri == null) {
                    Toast.makeText(getActivity().getApplicationContext(), "No Video selected", Toast.LENGTH_SHORT).show();
                    return;
                }
                String path = getRealPathFromURI(getActivity().getApplicationContext(),selectedImageUri);
                File file = new File(path);
                updateImageToServer(file, userId, trickId);
                selectedImageUri = null;
                getActivity().onBackPressed();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
                selectedImageUri = data.getData();

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

            }
        }
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void updateImageToServer(File file, String userId, String trickId) {
        // create upload service client
        final IUploadFiles service = Repository.getInstance().retrofit.create(IUploadFiles.class);

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("trickVideo", file.getName(), requestFile);

        // add another part within the multipart request
        String descriptionString = "hello, this is description speaking";
        RequestBody description =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), descriptionString);

        // finally, execute the request
        Call<ResponseBody> call = service.addTrickVideo(userId, trickId, description, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                Log.v("Upload", "success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });
    }

}

