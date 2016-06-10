package com.example.tiferet.polerbear.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.tiferet.polerbear.API.IUploadFiles;
import com.example.tiferet.polerbear.Picker.Image.ImagePicker;
import com.example.tiferet.polerbear.R;
import com.example.tiferet.polerbear.Repository.Server.Repository;
import com.example.tiferet.polerbear.Repository.Server.SessionManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditProfile extends AppCompatActivity {
    private static final int PICK_IMAGE_ID = 234; // the number doesn't matter

    ImageView profilePic;
    String[] sexPickerDropdown = new String[]{"Male", "Female"};
    SessionManager session;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        session = new SessionManager(getApplicationContext());
        session.checkLogin();
        final HashMap<String, String> user = session.getUserDetails();

        profilePic = (ImageView) findViewById(R.id.editProfileImage);
        //EditText newPic = (EditText) findViewById(R.id.addProfilePic);
        final Spinner sexDropdown = (Spinner) findViewById(R.id.dropdown);
        final ArrayAdapter<String> sexAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.dropdown_item_selected, sexPickerDropdown);
        sexAdapter.setDropDownViewResource(R.layout.dropdown_items);
        sexDropdown.setAdapter(sexAdapter);


        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // only photos (no videos) - camera OR gallery
                Intent chooseImageIntent = ImagePicker.getPickImageIntent(EditProfile.this.getApplicationContext());
                startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);

            }
        });

        Button save = (Button) findViewById(R.id.saveEditUserBtn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmap!=null){
                    File file = persistImage(bitmap, "name");
                    updateImageToServer(file, user);

//                    RequestBody fbody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//                    Log.d("Uplaod", String.valueOf(bitmap));
//                    final Call<Void> upload = apiUpload.addProfilePic(user.get(SessionManager.KEY_ID), fbody);
//                    upload.enqueue(new Callback<Void>() {
//                        @Override
//                        public void onResponse(Call<Void> call, Response<Void> response) {
//                            Log.d("Uplaod", "Sucess");
//                        }
//
//                        @Override
//                        public void onFailure(Call<Void> call, Throwable t) {
//                            Log.d("Uplaod", "FAIL");
//                        }
//                    });
                }

                onBackPressed();
                finish();
            }
        });

        Button cancel = (Button) findViewById(R.id.cancelBtn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }

    private void updateImageToServer(File file, HashMap<String, String> user) {
        // create upload service client
        final IUploadFiles service = Repository.getInstance().retrofit.create(IUploadFiles.class);

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", file.getName(), requestFile);

        // add another part within the multipart request
        String descriptionString = "hello, this is description speaking";
        RequestBody description =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), descriptionString);

        // finally, execute the request
        Call<ResponseBody> call = service.addProfilePic(user.get(SessionManager.KEY_ID), description, body);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case PICK_IMAGE_ID:
                bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                // TODO use bitmap
                //file = bitmap;
                profilePic.setImageBitmap(bitmap);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private File persistImage(Bitmap bitmap, String name) {
        File filesDir = getApplicationContext().getFilesDir();
        //File filesDir = getAppContext().getFilesDir();
        File imageFile = new File(filesDir, name + ".png");
        OutputStream os = null;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
        return imageFile;
    }

}
