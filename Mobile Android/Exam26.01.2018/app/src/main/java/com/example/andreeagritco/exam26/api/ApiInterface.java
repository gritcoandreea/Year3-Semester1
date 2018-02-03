package com.example.andreeagritco.exam26.api;

import com.example.andreeagritco.exam26.model.Project;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Andreea Gritco on 31-Jan-18.
 */

public interface ApiInterface {

    //Idea Section
    @GET("/ideas")
    Call<List<Project>> getIdeas();

    @POST("/add")
    Call<Project> addIdea(@Body Project project);

    @DELETE("delete/{id}")
    Call<Project> deleteIdea(@Path("id") int id);

    //Project Section
    @GET("/projects")
    Call<List<Project>> getProjects();

    @POST("/promote")
    Call<Project> promoteProject(@Body Project project);

    @POST("/approve")
    Call<Project> approveProject(@Body Project project);

    @POST("/discard")
    Call<Project> discardProject(@Body Project project);

    @DELETE("remove/{id}")
    Call<Project> removeProject(@Path("id") int id);
}
