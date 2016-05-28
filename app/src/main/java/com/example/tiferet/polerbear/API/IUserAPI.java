package com.example.tiferet.polerbear.API;

import com.example.tiferet.polerbear.Repository.Server.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by TIFERET on 14-May-16.
 */
public interface IUserAPI {

    //localhost:2070/Login?user={userId}&password={pwd}
    @GET("/Login")
    Call<User> Login(@Query("user") String user, @Query("pwd") String pwd);
    //193.106.55.28:443/isExisted?user=michael&email=michaelkolet@gmail.com&pwd=1234
    @GET("/isExisted")
    Call<Boolean> isExisted(@Query("user") String user, @Query("pwd") String pwd);
/*
    @GET("/addUser")
    Call<Integer> addUser(@Query("user") String user, @Query("pwd") String pwd, @Query("email") String email,@Query("birthdate") String birthdate, @Query("sex") String sex);
    //http://193.106.55.28:443/addUser?user=bla&pwd=1234&email=bla@gmail.com&birthdate=1987-07-14&sex=female
*/
    @POST("/addUser")
    Call<Integer> addUser(@Header("Content-Type") String content_type, @Body User user);

    @GET("/getFollowersCount")
    Call<Integer> getFollowersCount(@Query("user") Integer userId);
}
