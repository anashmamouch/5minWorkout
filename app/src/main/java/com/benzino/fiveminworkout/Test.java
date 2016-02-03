package com.benzino.fiveminworkout;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.io.Serializable;

/**
 * Created by Anas on 1/2/16.
 */
public class Test implements Serializable {
    private int id;
    private String type;
    private String createdAt;

    public Test(){

    }

    public Test(String type) {
        this.type = type;
    }

    public Test(int id, String type, String createdAt) {
        this.id = id;
        this.type = type;
        this.createdAt = createdAt;
    }

    public Test(String type, String createdAt) {
        this.type = type;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Test{" +
                "id=" + id +
                ", type=" + type +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }

}
