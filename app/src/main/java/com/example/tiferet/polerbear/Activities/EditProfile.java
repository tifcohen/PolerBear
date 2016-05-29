package com.example.tiferet.polerbear.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.tiferet.polerbear.R;

public class EditProfile extends AppCompatActivity {

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

        final Spinner sexDropdown = (Spinner) findViewById(R.id.dropdown);
        final ArrayAdapter<String> sexAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.dropdown_item_selected, sexPickerDropdown);
        sexAdapter.setDropDownViewResource(R.layout.dropdown_items);
        sexDropdown.setAdapter(sexAdapter);

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

}
