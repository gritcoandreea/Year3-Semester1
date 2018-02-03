package com.example.andreeagritco.exam26.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.andreeagritco.exam26.dao.ProjectDao;
import com.example.andreeagritco.exam26.model.Project;

/**
 * Created by Andreea Gritco on 31-Jan-18.
 */

@Database(entities = {Project.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase appDatabase;

    public static AppDatabase getInstance(Context context) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "project_ideas").build();
        }

        return appDatabase;

    }

    public abstract ProjectDao projectDao();
}
