package com.example.andreeagritco.exam.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by Andreea Gritco on 02-Feb-18.
 */

@Entity
public class Seat implements Serializable {

    @PrimaryKey
    private int id;

    private String name;

    @TypeConverters(TypeConv.class)
    private Type type;

    @NonNull
    @TypeConverters(StatusConv.class)
    private Status status;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @NonNull
    public Status getStatus() {
        return status;
    }

    public void setStatus(@NonNull Status status) {
        this.status = status;
    }
}
