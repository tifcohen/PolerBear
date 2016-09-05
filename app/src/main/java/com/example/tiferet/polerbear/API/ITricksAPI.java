package com.example.tiferet.polerbear.API;

import com.example.tiferet.polerbear.Repository.Server.Trick;
import com.example.tiferet.polerbear.Repository.Server.TrickForUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by TIFERET on 30-Apr-16.
 */
public interface ITricksAPI {

    //http://193.106.55.28:443/getTrick?trickId=1
    @GET("/getTrick")
    Call<Trick> getTrick(@Query("trickId") int trickId);

    @GET("/checkLevelGetTrick")
    Call<Trick> checkLevelGetTrick(@Query("user") int userId, @Query("trick") int trickId);

    //get all the trick in progress for user
    @GET("/getInProgress")
    Call<List<TrickForUser>> getInProgress(@Query("user") int userId);

    //http://193.106.55.28:443/getTrickInProgress?user=40&trick=5
    //get all progress for specific trick
    @GET("/getTrickInProgress")
    Call<List<TrickForUser>> getTrickInProgress(@Query("user") int userId, @Query("trick") int trickId);

    //localhost:2070/getTricksForUser?user={user}
    @GET("/getTricksForUser")
    Call<List<TrickForUser>> getTricksForUser(@Query("user") int userId);

    //localhost:2070/getAllTricksForUsers
    @GET("/getAllTricksForUsers")
    Call<List<TrickForUser>> getAllTricksForUsers();

    //Llist<TrickForUser> getAllTricksForFollowingUsers(userId)
    @GET("/getAllTricksForFollowingUsers")
    Call<List<TrickForUser>> getAllTricksForFollowingUsers(@Query("user") int userId);

    //localhost:2070/addProgress?user={userId}&trick={trickId}&pic={picRef}&date={dd/mm/yy}&comment={comment}
    @POST("/addProgress")
    Call<Void> addProgress(@Header("Content-Type") String content_type, @Body TrickForUser updatedProgress);

    @GET("/generateTrick")
    Call<Trick> generateTrick(@Query("user") int userId);

    //localhost:2070/checklevel?level={level}
    @GET("/checkLevel")
    Call<List<Trick>> checkLevel(@Query("level") int level);

    //level calLevel(userId, trickList)
    /*@POST("/calLevel")
    Call<Integer> calLevel(@Field("userId") int userId, @Field("trickList") List<Trick> doneBefore);*/

    @Multipart
    @POST("/calLevel")
    Call<Integer> calLevel(@Part("userId") int userId, @Part("trickList") List<Trick> doneBefore);
}

