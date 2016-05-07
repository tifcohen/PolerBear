package com.example.tiferet.polerbear.API;

import com.example.tiferet.polerbear.Model.gitmodel;

import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by TIFERET on 30-Apr-16.
 */
public interface gitapi {

    @GET("/users/{user}")
    public void getFeed(@Path("user") String user, Callback<gitmodel> response);
}
