package com.example.andreeagritco.exam25.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.andreeagritco.exam25.dao.GameDao;
import com.example.andreeagritco.exam25.model.Game;

/**
 * Created by Andreea Gritco on 01-Feb-18.
 */

@Database(entities = {Game.class}, version=1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase appDatabase;

    public abstract GameDao gameDao();

    public static AppDatabase getInstancce(Context context){
        if(appDatabase == null){
            appDatabase = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class, "games_database").build();
        }

        return appDatabase;
    }


}
