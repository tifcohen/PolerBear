package com.example.tiferet.polerbear.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.CursorLoader;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.tiferet.polerbear.API.ITricksAPI;
import com.example.tiferet.polerbear.API.IUploadFiles;
import com.example.tiferet.polerbear.Picker.Date.DateEditText;
import com.example.tiferet.polerbear.R;
import com.example.tiferet.polerbear.Repository.Server.Repository;
import com.example.tiferet.polerbear.Repository.Server.SessionManager;
import com.example.tiferet.polerbear.Repository.Server.Trick;
import com.example.tiferet.polerbear.Repository.Server.TrickForUser;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    SessionManager session;

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

        session = new SessionManager(getActivity().getApplicationContext());

        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        Button saveBtn = (Button) view.findViewById(R.id.saveBtn);
        Button cancelBtn = (Button) view.findViewById(R.id.cancelBtn);
        Button upload = (Button) view.findViewById(R.id.uploadBtn);
        final CheckBox cb = (CheckBox) view.findViewById(R.id.finished);
        final DateEditText progressDate = (DateEditText) view.findViewById(R.id.progressDate);
        trickImage = (ImageView) view.findViewById(R.id.uploadedImage);
        final TextView trickName = (TextView) view.findViewById(R.id.trickName);
        videoView = (VideoView) view.findViewById(R.id.videoView);
        final EditText comment = (EditText) view.findViewById(R.id.commentsBox);

        progressBar.setVisibility(View.GONE);
        videoView.setVisibility(View.GONE);
        trickImage.setVisibility(View.GONE);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        progressDate.setText(dateFormat.format(date));

        final ITricksAPI api = Repository.getInstance().retrofit.create(ITricksAPI.class);
        Call<Trick> callTrick = api.getTrick(Integer.parseInt(trickId));
        callTrick.enqueue(new Callback<Trick>() {
            @Override
            public void onResponse(Call<Trick> call, Response<Trick> response) {
                trickName.setText(response.body().getTrickName());
            }

            @Override
            public void onFailure(Call<Trick> call, Throwable t) {

            }
        });

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
                progressBar.setVisibility(View.VISIBLE);
                TrickForUser updatedProgress = new TrickForUser();
                updatedProgress.setUserId(Integer.parseInt(userId));
                updatedProgress.setUserName(session.getName());
                updatedProgress.setTrickId(Integer.parseInt(trickId));
                updatedProgress.setTrickName(trickName.getText().toString());
                updatedProgress.setDate(progressDate.getText().toString());
                updatedProgress.setComment(comment.getText().toString());
                if(cb.isChecked()){
                    updatedProgress.setIsFinished(1);
                }else {
                    updatedProgress.setIsFinished(0);
                }

                final ITricksAPI api = Repository.getInstance().retrofit.create(ITricksAPI.class);
                Call<Void> callTrick = api.addProgress("application/json", updatedProgress);
                callTrick.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(getContext(), "Progress updated successfully", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        getActivity().onBackPressed();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Update progress failed, please try again", Toast.LENGTH_SHORT).show();
                    }
                });

                if(selectedImageUri == null) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please select a video to upload", Toast.LENGTH_SHORT).show();
                    return;
                }
                //String path = getRealPathFromURI(getActivity().getApplicationContext(),selectedImageUri);
                String path = getRealPathFromURI(selectedImageUri);
                File file = new File(path);
                updateImageToServer(file, userId, trickId);
                selectedImageUri = null;
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
/*
    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        String tmp;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            tmp = cursor.getString(column_index);
            return tmp;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }*/

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getActivity().getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
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
//                Toast.makeText(getActivity().getApplicationContext(), "Video uploaded successfully", Toast.LENGTH_SHORT).show();
                Log.v("Upload", "success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
   //             Toast.makeText(getActivity().getApplicationContext(), "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                Log.e("Upload error:", t.getMessage());
            }
        });
    }

}

