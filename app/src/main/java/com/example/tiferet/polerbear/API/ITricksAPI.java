package com.example.tiferet.polerbear.API;

import com.example.tiferet.polerbear.Repository.Server.Trick;
import com.example.tiferet.polerbear.Repository.Server.TrickForUser;

import java.util.List;

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

    //get all the trick in progress for user
    @GET("/getInProgress")
    Call<List<TrickForUser>> getInProgress(@Query("user") int userId);

    //http://193.106.55.28:443/getTrickInProgress?user=40&trick=5
    //get all progress for specific trick
    @GET("/getTrickInProgres")
    Call<List<TrickForUser>> getTrickInProgress(@Query("user") int userId, @Query("trick") int trickId);
}

