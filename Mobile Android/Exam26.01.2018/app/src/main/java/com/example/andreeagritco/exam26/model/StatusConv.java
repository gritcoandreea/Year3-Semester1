package com.example.andreeagritco.exam26.model;

import android.arch.persistence.room.TypeConverter;

/**
 * Created by Andreea Gritco on 31-Jan-18.
 */

public class StatusConv {

    @TypeConverter
    public static Status toType(String type){
        return Status.valueOf(type);
    }

    @TypeConverter
    public static String toString(Status status){
        return status.toString();
    }

}
