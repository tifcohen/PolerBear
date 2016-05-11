package com.example.tiferet.polerbear.API;

import com.example.tiferet.polerbear.Repository.Server.Trick;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by TIFERET on 30-Apr-16.
 */
public interface gitapi {

    //193.106.55.28:443/isExisted?user=michael&email=michaelkolet@gmail.com&pwd=1234
    /*GET("isExisted?")      //here is the other url part.best way is to start using /
    //Call<User> isExisted(@Query("user")  String user, @Query("email") String email, @Query("pwd") String pwd, Callback<User> response);
    Call<User> isExisted(@Path("user") String user,Callback<User> response);     //string user is for passing values from edittext for eg: user=basil2style,google
    //response is the response from the server which is now in the POJO*/

    //http://193.106.55.28:443/getTrick?trickId=1
    @GET("/{trickId}")
    Call<Trick> getTrick(@Path("trickId") int trickId);
}

