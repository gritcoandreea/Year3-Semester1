package com.example.andreeagritco.exam.model;

import android.arch.persistence.room.TypeConverter;

/**
 * Created by Andreea Gritco on 02-Feb-18.
 */

public class TypeConv {

    @TypeConverter
    public static Type toType(String type){
        return Type.valueOf(type);
    }

    @TypeConverter
    public static String toString(Type type){
        return type.toString();
    }

}
