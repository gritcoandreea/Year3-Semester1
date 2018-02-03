package com.example.andreeagritco.exam25.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by Andreea Gritco on 01-Feb-18.
 */

@Entity(primaryKeys ={"id","status"})
public class Game implements Serializable {

   // @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    private String name;

    private int quantity;

    @TypeConverters(TypeConv.class)
    private Type type;

    @NonNull
    @TypeConverters(StatusConv.class)
    private Status status;

    @Ignore
    public Game(int id, String name, int quantity, Type type, Status status) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.type = type;
        this.status = status;
    }

    public Game(String name, int quantity, Type type, Status status) {
        this.name = name;
        this.quantity = quantity;
        this.type = type;
        this.status = status;
    }


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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
