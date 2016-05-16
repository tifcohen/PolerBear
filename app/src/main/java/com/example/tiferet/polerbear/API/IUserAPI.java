package com.example.tiferet.polerbear.API;

import com.example.tiferet.polerbear.Repository.Server.User;

import retrofit2.Call;
import retrofit2.http.GET;
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
    Call<Boolean> isExisted(@Query("user") String user, @Query("email") String email, @Query("pwd") String pwd);

    //193.106.55.28:443/Login?user=michael&password=1234
}
