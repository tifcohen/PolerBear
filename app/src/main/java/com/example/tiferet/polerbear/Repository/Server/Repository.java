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

/*
    public Trick foo() {
        int trickId = 1;
        final Trick[] trick = new Trick[1];
        ITricksAPI api = retrofit.create(ITricksAPI.class);
        Call<Trick> call = api.getTrick(trickId);

        call.enqueue(new Callback<Trick>() {
            @Override
            public void onResponse(Call<Trick> call, Response<Trick> response) {
                Log.d("TAG", "pass");
                trick[0] = response.body();
                Log.d("TAG", ""+trick[0].getTrickName());
            }

            @Override
            public void onFailure(Call<Trick> call, Throwable t) {
                Log.d("TAG", "fail");
            }
        });

        String user = "michael";
        String email = "michaelkolet@gmail.com";
        String pwd = "1234";


        return trick[0];
    }*/
}