package com.example.andreeagritco.exam.model;

import java.io.Serializable;

/**
 * Created by Andreea Gritco on 02-Feb-18.
 */

public class IdObject implements Serializable {

    private int id;

    public IdObject(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
