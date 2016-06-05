package com.example.tiferet.polerbear.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.tiferet.polerbear.Picker.Image.ImagePicker;
import com.example.tiferet.polerbear.R;


public class EditProfile extends AppCompatActivity {
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 1234;

    private static final int PICK_IMAGE_ID = 234; // the number doesn't matter


    String userChoosenTask;
    ImageView profilePic;
    String[] sexPickerDropdown = new String[]{"Male", "Female"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        profilePic = (ImageView) findViewById(R.id.editProfileImage);
        EditText newPic = (EditText) findViewById(R.id.addProfilePic);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case PICK_IMAGE_ID:
                Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                // TODO use bitmap
                profilePic.setImageBitmap(bitmap);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
}
