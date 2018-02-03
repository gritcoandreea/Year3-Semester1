package com.example.andreeagritco.exam.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.andreeagritco.exam.model.Seat;

import java.util.List;

/**
 * Created by Andreea Gritco on 02-Feb-18.
 */

@Dao
public interface SeatDao {

    @Query("Select * from seat")
    List<Seat> getAll();

    @Insert
    void add(Seat... games);

    @Delete
    void delete(Seat... games);

    @Update
    void update(Seat... games);

}
