package com.example.andreeagritco.exam.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.andreeagritco.exam.dao.SeatDao;
import com.example.andreeagritco.exam.model.Seat;

/**
 * Created by Andreea Gritco on 02-Feb-18.
 */

@Database(entities = {Seat.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase appDatabase;

    public abstract SeatDao seatDao();

    public static AppDatabase getInstancce(Context context) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "seat_database").build();
        }

        return appDatabase;
    }

}
