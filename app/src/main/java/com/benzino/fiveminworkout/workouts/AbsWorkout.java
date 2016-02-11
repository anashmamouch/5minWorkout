package com.benzino.fiveminworkout.workouts;

import com.benzino.fiveminworkout.R;

/**
 * Created on 7/2/16.
 *
 * @author Anas
 */
public class AbsWorkout extends WorkoutActivity {
    /**
     * Used to set the title of the toolbar
     *
     * @return the title of the toolbar
     */
    @Override
    protected String setToolbarTitle() {
        return "Abs Workout";
    }

    /**
     * Used to set the array of exercices
     */
    @Override
    protected void setExercises() {
        workouts = new String[]{
                getResources().getString(R.string.abs_ex_1_title),
                getResources().getString(R.string.abs_ex_2_title),
                getResources().getString(R.string.abs_ex_3_title),
                getResources().getString(R.string.abs_ex_4_title),
                getResources().getString(R.string.abs_ex_5_title),
                getResources().getString(R.string.abs_ex_6_title),
                getResources().getString(R.string.abs_ex_7_title),
                getResources().getString(R.string.abs_ex_8_title),
                getResources().getString(R.string.abs_ex_9_title),
                getResources().getString(R.string.abs_ex_10_title),
                " "};
    }

    /**
     * Used to set the array of videos
     */
    @Override
    protected void setVideos() {
        videos = new String[]{
                getResources().getString(R.string.abs_ex_1_url),
                getResources().getString(R.string.abs_ex_2_url),
                getResources().getString(R.string.abs_ex_3_url),
                getResources().getString(R.string.abs_ex_4_url),
                getResources().getString(R.string.abs_ex_5_url),
                getResources().getString(R.string.abs_ex_6_url),
                getResources().getString(R.string.abs_ex_7_url),
                getResources().getString(R.string.abs_ex_8_url),
                getResources().getString(R.string.abs_ex_9_url),
                getResources().getString(R.string.abs_ex_10_url),
                " "
        };
    }

    /**
     * Sets the type of the workout
     *
     * @return type of the workout
     */
    @Override
    protected String setType() {
        return "ABS";
    }
}
