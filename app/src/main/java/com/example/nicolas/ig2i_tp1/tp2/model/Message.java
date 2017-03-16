package com.example.nicolas.ig2i_tp1.tp2.model;

import android.graphics.Color;

/**
 * Created by Nicolas on 01/03/2017.
 */

public class Message {

    private int id;
    private String text;
    private String author;
    private int color;

    public Message(int id, String text, String author, int color) {
        this.id = id;
        this.text = text;
        this.author = author;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
