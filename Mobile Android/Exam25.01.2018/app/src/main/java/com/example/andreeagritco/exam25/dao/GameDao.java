package com.example.andreeagritco.exam25.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.andreeagritco.exam25.model.Game;

import java.util.List;

import retrofit2.http.DELETE;

/**
 * Created by Andreea Gritco on 01-Feb-18.
 */

@Dao
public interface GameDao {

    @Query("Select * from game")
    List<Game> getAll();

    @Insert
    void add(Game... games);

    @Delete
    void delete(Game... games);

    @Update
    void update(Game... games);


}
