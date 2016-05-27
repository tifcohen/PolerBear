package com.example.tiferet.polerbear.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.tiferet.polerbear.API.IUserAPI;
import com.example.tiferet.polerbear.R;
import com.example.tiferet.polerbear.Repository.Server.Repository;
import com.example.tiferet.polerbear.Repository.Server.SessionManager;
import com.example.tiferet.polerbear.Repository.Server.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        session = new SessionManager(getApplicationContext());

        final IUserAPI api = Repository.getInstance().retrofit.create(IUserAPI.class);
        final EditText username = (EditText) findViewById(R.id.userLogin);
        final EditText pwd = (EditText) findViewById(R.id.pswLogin);

        Button login = (Button) findViewById(R.id.loginBtn);
        final ProgressBar loginPB = (ProgressBar) findViewById(R.id.progressBar);
        loginPB.setVisibility(View.GONE);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().equals("")||pwd.getText().toString().equals("")){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getApplicationContext());
                    alertDialogBuilder.setTitle("Action Failed");
                    alertDialogBuilder.setMessage("Please fill the requested fields").setCancelable(false)
                            .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    alertDialog.getWindow().setDimAmount(0.5f);
                    alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                }
                else{
                    loginPB.setVisibility(View.VISIBLE);
                    final Call<User> call = api.Login(username.getText().toString(), pwd.getText().toString());
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            User user = response.body();
                            session.createLoginSession(user.getUserId().toString(),user.getUserName(),user.getUserEmail(),user.getUserBirthDate(), user.getUserLevel().toString());
                            Intent intent = new Intent(getApplicationContext(), MyProfile.class);
                            startActivity(intent);
                            finish();
                            loginPB.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Login.this);
                            alertDialogBuilder.setTitle("Action Failed");
                            alertDialogBuilder.setMessage("There is no user by that name or the password is not correct").setCancelable(false)
                                    .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                        }
                                    });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                            alertDialog.getWindow().setDimAmount(0.5f);
                            alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                        }
                    });
                }
            }
        });

      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickSignUp(View v){
        Intent intent = new Intent(getApplicationContext(), SignUp.class);
        startActivity(intent);
    }
}
