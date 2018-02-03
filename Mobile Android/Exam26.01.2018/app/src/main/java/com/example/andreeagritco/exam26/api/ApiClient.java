package com.example.andreeagritco.exam26.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Andreea Gritco on 31-Jan-18.
 */

public class ApiClient {


    private static final String BASE_URL = "http://192.168.1.5:4026/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient(){
        if(retrofit == null){
            retrofit =  new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }


}
