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
import com.example.tiferet.polerbear.API.IUserAPI;
import com.example.tiferet.polerbear.Picker.Date.DateEditText;
import com.example.tiferet.polerbear.Picker.Image.ImagePicker;
import com.example.tiferet.polerbear.R;
import com.example.tiferet.polerbear.Repository.Server.Repository;
import com.example.tiferet.polerbear.Repository.Server.SessionManager;
import com.example.tiferet.polerbear.Repository.Server.User;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditProfile extends AppCompatActivity {
    private static final int PICK_IMAGE_ID = 234; // the number doesn't matter
    final public static int EDIT_PROFILE = 456;
    ImageView profilePic;
    String[] sexPickerDropdown = new String[]{"Male", "Female"};
    SessionManager session;
    Bitmap bitmap;
    int flag = 0;

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

        final DateEditText date = (DateEditText) findViewById(R.id.editUserBirthDate);
        profilePic = (ImageView) findViewById(R.id.editProfileImage);
        //EditText newPic = (EditText) findViewById(R.id.addProfilePic);
        final Spinner sexDropdown = (Spinner) findViewById(R.id.dropdown);
        final ArrayAdapter<String> sexAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.dropdown_item_selected, sexPickerDropdown);
        sexAdapter.setDropDownViewResource(R.layout.dropdown_items);
        sexDropdown.setAdapter(sexAdapter);
        String compareValue = session.getSex();
        if (!compareValue.equals(null)) {
            int spinnerPosition = sexAdapter.getPosition(compareValue);
            sexDropdown.setSelection(spinnerPosition);
        }

        IUserAPI apiUser = Repository.getInstance().retrofit.create(IUserAPI.class);
        Call<String> picRefCall = apiUser.getUserProfilePicName(session.getUserId().toString());
        picRefCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                final IUploadFiles apiPic = Repository.getInstance().retrofit.create(IUploadFiles.class);
                Call<String> picCall = apiPic.getFile(response.body());
                picCall.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.body()==null){
                            if(session.getSex().equals("Female")) {
                                profilePic.setImageDrawable(getResources().getDrawable(R.drawable.female));
                            }else{
                                profilePic.setImageDrawable(getResources().getDrawable(R.drawable.male));
                            }
                        }else{
                            UrlImageViewHelper.setUrlDrawable(profilePic, response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("Profile Pic", t.getMessage());
                    }
                });
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Profile Pic ref", t.getMessage());
            }
        });


        date.setText(session.getBrithdate());

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 1;
                // only photos (no videos) - camera OR gallery
                Intent chooseImageIntent = ImagePicker.getPickImageIntent(EditProfile.this.getApplicationContext());
                startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);

            }
        });

        Button save = (Button) findViewById(R.id.saveEditUserBtn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final IUserAPI userAPI = Repository.getInstance().retrofit.create(IUserAPI.class);
                final User updatedUser = new User();
                updatedUser.setUserId(session.getUserId());
                updatedUser.setUserSex(sexDropdown.getSelectedItem().toString());
                updatedUser.setUserBirthDate(date.getText().toString());

                Call<Void> call = userAPI.updateUser("application/json", updatedUser);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        session.updateSession(updatedUser.getUserBirthDate(), updatedUser.getUserSex());
                        Intent updatedProfile = new Intent();
                        setResult(EDIT_PROFILE, updatedProfile);

                        // update image if need
                        if (bitmap!=null && flag==1){
                            File file = persistImage(bitmap, "name");
                            updateImageToServer(file, session.getUserId());
                        } else {
                            finish();
                        }

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });

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

    private void updateImageToServer(File file, Integer userId) {
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
        Call<ResponseBody> call = service.addProfilePic(userId.toString(), description, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                Log.v("Upload", "success");
                finish();
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
