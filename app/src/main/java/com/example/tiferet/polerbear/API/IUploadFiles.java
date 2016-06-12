package com.example.tiferet.polerbear.API;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by TIFERET on 09-Jun-16.
 */
public interface IUploadFiles {
//
//    @Multipart
//    @POST("addProfilePic")
//    Call<Void> addProfilePic(@Part("user") String userId, @Part("myfile\"; filename=\"image.png\"") RequestBody imageFile);

    @Multipart
    @POST("addProfilePic")
    Call<ResponseBody> addProfilePic(@Part("user") String userId,
                              @Part("description") RequestBody description,
                              @Part MultipartBody.Part file);

    @Multipart
    @POST("addTrickVideo")
    Call<ResponseBody> addTrickVideo(@Part("user") String userId,
                                     @Part("trick") String trickIdId,
                                     @Part("description") RequestBody description,
                                     @Part MultipartBody.Part file);

    //http://193.106.55.28:443/getFile?fileName=40_26_46
    @GET("/getFile")
    Call<String> getFile(@Query("fileName") String fileName);
}