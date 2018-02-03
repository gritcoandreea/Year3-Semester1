package com.example.andreeagritco.exam.api;

import com.example.andreeagritco.exam.model.IdObject;
import com.example.andreeagritco.exam.model.Seat;
import com.example.andreeagritco.exam.model.Status;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Andreea Gritco on 02-Feb-18.
 */

public interface ApiInterface {

    //CLIENT SECTION

    @GET("/seats")
    Call<List<Seat>> getSeats();

    @POST("/reserve")
    Call<Seat> reserveSeat(@Body IdObject idObject);

    @GET("/refresh/{id}")
    Call<Status> refreshSeatStatus(@Path("id") int id);

    @POST("/buy")
    Call<Seat> buySeat(@Body IdObject idObject);


    //THEATER SECTION

    @GET("/all")
    Call<List<Seat>> getAll();

    @POST("/confirm")
    Call<Seat> confirmSeat(@Body IdObject idObject);

    @DELETE("/clean")
    Call<Void> deleteSeatsMarkAvailable();

    //ADMIN

    @GET("/confirmed")
    Call<List<Seat>> getConfirmedSeats();

    @GET("/taken")
    Call<List<Seat>> getPurchasedSeats();

    @DELETE("/zap")
    Call<Void> deleteAllSeats();


}
