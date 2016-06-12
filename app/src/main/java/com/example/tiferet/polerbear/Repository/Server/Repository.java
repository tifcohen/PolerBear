package com.example.tiferet.polerbear.Repository.Server;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by TIFERET on 09-May-16.
 */
public class Repository {
    private final static Repository instance = new Repository();
    String API = "http://193.106.55.28:443/";
    public Retrofit retrofit;

    public Repository() {
        retrofit = new Retrofit.Builder().baseUrl(API)
                .addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static Repository getInstance(){
        return instance;
    }

}