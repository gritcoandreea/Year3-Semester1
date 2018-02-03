package com.example.andreeagritco.exam26.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.andreeagritco.exam26.model.Project;

import java.util.List;

/**
 * Created by Andreea Gritco on 31-Jan-18.
 */

@Dao
public interface ProjectDao {

    @Query("Select * from project")
    List<Project> getAll();

    @Query("Delete from project")
    void deleteAll();

    @Insert
    void add(Project ... projects);

    @Delete
    void delete(Project... projects);

}
