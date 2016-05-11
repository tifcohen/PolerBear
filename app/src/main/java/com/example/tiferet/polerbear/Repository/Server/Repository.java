package com.example.tiferet.polerbear.Repository.Server;


import com.example.tiferet.polerbear.API.gitapi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by TIFERET on 09-May-16.
 */
public class Repository {
    String API = "http://193.106.55.28:443/";
    Retrofit retrofit;

    public Repository() {
        //restAdapter.Builder() = new Retrofit().Builder().setLogLevel(RestAdapter.LogLevel.FULL).setEndpoint(API).build();
        retrofit = new Retrofit.Builder().baseUrl(API)
                .addConverterFactory(GsonConverterFactory.create()).build();

    }

    public Trick foo() {
        gitapi git = retrofit.create(gitapi.class);
        //String user = "michael";
        String user = "user=michael&email=michaelkolet@gmail.com&pwd=1234";
        String email = "michaelkolet@gmail.com";
        String pwd = "1234";
        int trickId = 1;

        git.getTrick(trickId);

        return null;
    }

    /*
        git.isExisted(user, email, pwd, new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d("TAG", "pass");
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("TAG", "fail");
            }
        });
        return null;
    }*/


}