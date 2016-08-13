package com.example.tiferet.polerbear.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tiferet.polerbear.API.ITricksAPI;
import com.example.tiferet.polerbear.API.IUploadFiles;
import com.example.tiferet.polerbear.API.IUserAPI;
import com.example.tiferet.polerbear.R;
import com.example.tiferet.polerbear.Repository.Server.Repository;
import com.example.tiferet.polerbear.Repository.Server.SessionManager;
import com.example.tiferet.polerbear.Repository.Server.TrickForUser;
import com.example.tiferet.polerbear.Repository.Server.User;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProfile extends AppCompatActivity implements View.OnClickListener {

    int flag = 0;
    final Context context = this;
    List<TrickForUser> tricks;
    SessionManager session;
    final public static int UPDATE_PROGRESS = 123;
    final public static int EDIT_PROFILE = 456;
    ListView trickList;
    ImageView profilePic;

    private SubActionButton btnNewTrick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        session = new SessionManager(getApplicationContext());
        //session.checkLogin();
        profilePic = (ImageView) findViewById(R.id.myProfilePicture);
        final TextView username = (TextView) findViewById(R.id.myProfileUsername);
        final TextView userLevel = (TextView) findViewById(R.id.myProfileLevelView);
        final TextView userFollowers = (TextView) findViewById(R.id.myProfileFollowers);
        final TextView progressTitle = (TextView) findViewById(R.id.progress_title);
        trickList = (ListView) findViewById(R.id.trickInProgressList);
        final Button followBtn = (Button) findViewById(R.id.followBtn);
        followBtn.setVisibility(View.GONE);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.logo_transparent);

        final String ref = getIntent().getStringExtra("ref");
        if (ref != null) {
            if (!ref.equals(session.getUserId().toString())) {
                flag = 1;
                followBtn.setVisibility(View.VISIBLE);
                progressTitle.setText(R.string.progress_title_other_profile);
                IUserAPI otherApi = Repository.getInstance().retrofit.create(IUserAPI.class);
                Call<User> callOther = otherApi.getUser(Integer.parseInt(ref));
                callOther.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        final User other = response.body();
                        fab.setVisibility(View.GONE);
                        username.setText(other.getUserName());
                        userLevel.setText("Level: " + other.getUserLevel());
                        Log.d("profile", "other's profile! " + other.getUserId());

                        final IUserAPI apiUser = Repository.getInstance().retrofit.create(IUserAPI.class);
                        Call<Boolean> followCall = apiUser.isFollowingList(session.getUserId(), other.getUserId());
                        followCall.enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                if (response.body()) {
                                    followBtn.setText("Unfollow");
                                    followBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Call<Void> unfollow = apiUser.removeFollowingList(session.getUserId(), other.getUserId());
                                            unfollow.enqueue(new Callback<Void>() {
                                                @Override
                                                public void onResponse(Call<Void> call, Response<Void> response) {
                                                    followBtn.setText("Follow");

                                                    Toast.makeText(getApplicationContext(), "Unfollow " + other.getUserName(), Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onFailure(Call<Void> call, Throwable t) {

                                                }
                                            });
                                            Call<Void> follow = apiUser.addToFollowingList(session.getUserId(), other.getUserId());
                                            follow.enqueue(new Callback<Void>() {
                                                @Override
                                                public void onResponse(Call<Void> call, Response<Void> response) {
                                                    followBtn.setText("Unfollow");
                                                    Toast.makeText(getApplicationContext(), "Following " + other.getUserName(), Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onFailure(Call<Void> call, Throwable t) {

                                                }
                                            });
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {

                            }
                        });
                        Call<String> picRefCall = apiUser.getUserProfilePicName(other.getUserId().toString());
                        picRefCall.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                final IUploadFiles apiPic = Repository.getInstance().retrofit.create(IUploadFiles.class);
                                Call<String> picCall = apiPic.getFile(response.body());
                                picCall.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        if (response.body() == null) {
                                            if (other.getUserSex().equals("Female")) {
                                                profilePic.setImageDrawable(getResources().getDrawable(R.drawable.female));
                                            } else {
                                                profilePic.setImageDrawable(getResources().getDrawable(R.drawable.male));
                                            }
                                        } else {
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
                        final ITricksAPI api = Repository.getInstance().retrofit.create(ITricksAPI.class);
                        final Call<List<TrickForUser>> callTrickForUser = api.getTricksForUser(other.getUserId());

                        callTrickForUser.enqueue(new Callback<List<TrickForUser>>() {
                            @Override
                            public void onResponse(Call<List<TrickForUser>> call, Response<List<TrickForUser>> response) {
/*                              #TODO gallery for the other user
                                tricks = response.body();
                                TricksInProgressAdapter adapter = new TricksInProgressAdapter();
                                trickList.setAdapter(adapter);
                                //spinner.setVisibility(View.GONE);*/
                            }

                            @Override
                            public void onFailure(Call<List<TrickForUser>> call, Throwable t) {
                                Log.d("TAG", "failed to fetch tricks");
                            }
                        });
                        trickList = (ListView) findViewById(R.id.galleryList);
                        final Call<Integer> callUser = apiUser.getFollowersCount(other.getUserId());
                        callUser.enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                if (response.body() == null) {
                                    userFollowers.setText("This user has no followers");
                                } else {
                                    userFollowers.setText(response.body() + " Followers");
                                }
                            }

                            @Override
                            public void onFailure(Call<Integer> call, Throwable t) {

                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });
            }
        } else {
            progressTitle.setText(R.string.progress_title_my_profile);
            username.setText(session.getName());
            userLevel.setText("Level: " + session.getLevel());

            final IUserAPI apiUser = Repository.getInstance().retrofit.create(IUserAPI.class);
            Call<String> picRefCall = apiUser.getUserProfilePicName(session.getUserId().toString());
            picRefCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    final IUploadFiles apiPic = Repository.getInstance().retrofit.create(IUploadFiles.class);
                    Call<String> picCall = apiPic.getFile(response.body());
                    picCall.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.body() == null) {
                                if (session.getSex().equals("Female")) {
                                    profilePic.setImageDrawable(getResources().getDrawable(R.drawable.female));
                                } else {
                                    profilePic.setImageDrawable(getResources().getDrawable(R.drawable.male));
                                }
                            } else {
                                Log.d("profile pic", response.body().toString());
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
            final Call<Integer> callUser = apiUser.getFollowersCount(session.getUserId());
            callUser.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if (response.body() == null) {
                        userFollowers.setText("You don't have any followers");
                    } else {
                        userFollowers.setText(response.body() + " Followers");
                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {

                }
            });
            ITricksAPI apiTrick = Repository.getInstance().retrofit.create(ITricksAPI.class);
            Call<List<TrickForUser>> trickCall = apiTrick.getInProgress(session.getUserId());

            trickCall.enqueue(new Callback<List<TrickForUser>>() {
                @Override
                public void onResponse(Call<List<TrickForUser>> call, Response<List<TrickForUser>> response) {
                    tricks = response.body();
                    TricksInProgressAdapter adapter = new TricksInProgressAdapter();
                    trickList.setAdapter(adapter);
                }

                @Override
                public void onFailure(Call<List<TrickForUser>> call, Throwable t) {

                }
            });

            trickList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("My Profile", "row selected " + position);
                    TrickForUser trick = tricks.get(position);
                    Intent intent = new Intent(getApplicationContext(), Progress.class);
                    intent.putExtra("trickId", trick.getTrickId().toString());
                    startActivityForResult(intent, UPDATE_PROGRESS);
                }
            });

        }

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        ImageView itemIcon1 = new ImageView(this);
        ImageView itemIcon2 = new ImageView(this);
        ImageView itemIcon3 = new ImageView(this);
        ImageView itemIcon4 = new ImageView(this);

        itemIcon1.setImageResource(R.drawable.newsfeed_icon);
        itemIcon2.setImageResource(R.drawable.warmup_icon);
        itemIcon3.setImageResource(R.drawable.gallery_icon);
        itemIcon4.setImageResource(R.drawable.add_new_trick);

        SubActionButton btnNewsfeed = itemBuilder.setContentView(itemIcon1).build();
        SubActionButton btnWarmup = itemBuilder.setContentView(itemIcon2).build();
        SubActionButton btnGallery = itemBuilder.setContentView(itemIcon3).build();
        btnNewTrick = itemBuilder.setContentView(itemIcon4).build();

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(btnNewsfeed).addSubActionView(btnWarmup).addSubActionView(btnGallery).addSubActionView(btnNewTrick)
                .attachTo(fab)
                .build();

        btnWarmup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewTrick.class);
                intent.putExtra("ref", "warmup");
                startActivity(intent);
            }
        });

        btnNewsfeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Newsfeed.class);
                startActivity(intent);
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Gallery.class);
                startActivity(intent);
            }
        });

        btnNewTrick.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editBtn: {
                Intent intent = new Intent(getApplicationContext(), EditProfile.class);
                startActivityForResult(intent, EDIT_PROFILE);
                return true;
            }
            case R.id.logoutBtn: {
                session.logoutUser();
                return true;
            }
            case R.id.myProfile: {
                Intent intent = new Intent(getApplicationContext(), MyProfile.class);
                startActivity(intent);
            }
            default:
                //return super.onOptionsItemSelected(item);
                return true;
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem myProfile = menu.findItem(R.id.myProfile);
        MenuItem logout = menu.findItem(R.id.logoutBtn);
        MenuItem edit = menu.findItem(R.id.editBtn);

        if (flag == 1) {
            // myProfile.setEnabled(true);
            myProfile.setVisible(true);
            logout.setVisible(false);
            edit.setVisible(false);
        } else {
            // disabled
            myProfile.setVisible(false);
            //myProfile.setEnabled(false);
            logout.setEnabled(true);
            edit.setEnabled(true);
//            item.getIcon().setAlpha(130);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == UPDATE_PROGRESS) {
            ITricksAPI apiTrick = Repository.getInstance().retrofit.create(ITricksAPI.class);
            Call<List<TrickForUser>> trickCall = apiTrick.getInProgress(session.getUserId());

            trickCall.enqueue(new Callback<List<TrickForUser>>() {
                @Override
                public void onResponse(Call<List<TrickForUser>> call, Response<List<TrickForUser>> response) {
                    tricks = response.body();
                    TricksInProgressAdapter adapter = new TricksInProgressAdapter();
                    trickList.setAdapter(adapter);
                }

                @Override
                public void onFailure(Call<List<TrickForUser>> call, Throwable t) {

                }
            });
        }

        if (requestCode == EDIT_PROFILE) {
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
                            if (response.body() == null) {
                                if (session.getSex().equals("Female")) {
                                    profilePic.setImageDrawable(getResources().getDrawable(R.drawable.female));
                                } else {
                                    profilePic.setImageDrawable(getResources().getDrawable(R.drawable.male));
                                }
                            } else {
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
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(btnNewTrick)) {
            addNewTrick(v);
        } else {}
    }


    private void addNewTrick(final View v) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        // set title
        alertDialogBuilder.setTitle("Add a new trick:");
        // set dialog message
        alertDialogBuilder.setMessage("Do you want to do it yourself or let us do it?").setCancelable(false)
                .setPositiveButton("Do it!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getApplicationContext(), NewTrick.class);
                        intent.putExtra("ref", "Do it!");
                        startActivity(intent);
                        Snackbar.make(v, "Do it!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                })
                .setNegativeButton("I'll do it", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getApplicationContext(), NewTrick.class);
                        intent.putExtra("ref", "I'll do it");
                        startActivity(intent);
                        Snackbar.make(v, "I'll do it", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Snackbar.make(v, "Cancel", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getWindow().setDimAmount(0.5f);
        alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
    }



    class TricksInProgressAdapter extends BaseAdapter {

        public TricksInProgressAdapter() {
        }

        @Override
        public int getCount() { //returns the size of the list
            return tricks.size();
        }

        @Override
        public Object getItem(int position) { //returns the post
            return tricks.get(position);
        }

        @Override
        public long getItemId(int position) { //returns post id
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.my_progress_single_trick, null);
            }
            final TextView trickName = (TextView) convertView.findViewById(R.id.trickName);
            final TextView sinceDate = (TextView) convertView.findViewById(R.id.sinceDate);
            /*final TextView bookReview = (TextView) convertView.findViewById(R.id.bookReview);
            final ImageView stars = (ImageView) convertView.findViewById(R.id.stars);
            final TextView page = (TextView) convertView.findViewById(R.id.pageTextView);
            final TextView action = (TextView) convertView.findViewById(R.id.actionTextView);
            final TextView action2 = (TextView) convertView.findViewById(R.id.action2TextView);
            final ImageView userProfileImage = (ImageView) convertView.findViewById(R.id.userProfileImage);*/

            TrickForUser trick = tricks.get(position);
            trickName.setText(trick.getTrickName());
            sinceDate.setText(trick.getDate());

            return convertView;
        }
    }
}