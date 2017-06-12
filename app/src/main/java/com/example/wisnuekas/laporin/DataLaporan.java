package com.example.wisnuekas.laporin;

import java.util.ArrayList;

/**
 * Created by wisnuekas on 6/12/17.
 */

public class DataLaporan {
    private int id;
    private String imageUrl;
    private String nameImg;
    private String annotation;
    private String coordinate;
    private String date;

    //Getters and Setters
    public int getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNameImg() {
        return nameImg;
    }

    public void setNameImg(String nameImg) {
        this.nameImg = nameImg;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
