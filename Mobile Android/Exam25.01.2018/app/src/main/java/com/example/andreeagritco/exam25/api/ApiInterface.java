package com.example.andreeagritco.exam25.api;

import com.example.andreeagritco.exam25.model.Game;
import com.example.andreeagritco.exam25.model.IdObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Andreea Gritco on 01-Feb-18.
 */

public interface ApiInterface {

    @GET("/games")
    Call<List<Game>> getGames();

    @POST("/buyGame")
    Call<Game> buyGame(@Body IdObject idObject);

    @POST("/rentGame")
    Call<Game> rentGame(@Body IdObject idObject);

    @GET("/all")
    Call<List<Game>> getAll();

    @POST("/addGame")
    Call<Game> addGame(@Body Game game);

    @POST("/removeGame/")
    Call<Game> removeGame(@Body IdObject idObject);


}
