package com.example.andreeagritco.exam26.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.io.Serializable;

/**
 * Created by Andreea Gritco on 31-Jan-18.
 */

@Entity
public class Project implements Serializable{

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private int budget;

    @TypeConverters(TypeConv.class)
    private Type type;

    @TypeConverters(StatusConv.class)
    private Status status;

    public Project(String name, int budget, Type type, Status status) {
        this.name = name;
        this.budget = budget;
        this.type = type;
        this.status = status;
    }

    @Ignore
    public Project(String name, Integer bugdet, Type type) {
        this.budget=bugdet;
        this.name=name;
        this.type=type;
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

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
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
