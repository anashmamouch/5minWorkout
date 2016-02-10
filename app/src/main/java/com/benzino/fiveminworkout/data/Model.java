package com.benzino.fiveminworkout.data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Model class provides data for individual workouts
 *
 * Created by Anas on 27/1/16.
 */
public class Model {
    public String mTitle ;
    public String mDescription;
    public String mUrl;

    public Model(String mTitle, String mDescription, String mUrl) {
        this.mTitle = mTitle;
        this.mDescription = mDescription;
        this.mUrl = mUrl;
    }

    public Model(String mTitle, String mDescription) {
        this.mTitle = mTitle;
        this.mDescription = mDescription;
    }


    @Override
    public String toString() {
        return "Model{" +
                "mTitle='" + mTitle + '\'' +
                ", mDescription='" + mDescription + '\'' +
                ", mUrl='" + mUrl + '\'' +
                '}';
    }
}
