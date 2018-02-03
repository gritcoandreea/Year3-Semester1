package com.example.andreeagritco.exam25.model;

import java.io.Serializable;

/**
 * Created by Andreea Gritco on 01-Feb-18.
 */

public class IdObject implements Serializable {
    private int id;
    private int quantity;

    public IdObject(int id) {
        this.id = id;
    }

    public IdObject(int id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
