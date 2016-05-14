package com.example.tiferet.polerbear.API;

import com.example.tiferet.polerbear.Repository.Server.Trick;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by TIFERET on 30-Apr-16.
 */
public interface ITricksAPI {

    //http://193.106.55.28:443/getTrick?trickId=1
    @GET("/getTrick")
    Call<Trick> getTrick(@Query("trickId") int trickId);
}
