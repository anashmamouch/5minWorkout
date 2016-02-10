package com.benzino.fiveminworkout.workouts;

import com.benzino.fiveminworkout.R;

/**
 * Created on 7/2/16.
 *
 * @author Anas
 */
public class FatCardioWorkout extends WorkoutActivity {
    /**
     * Used to set the title of the toolbar
     *
     * @return the title of the toolbar
     */
    @Override
    protected String setToolbarTitle() {
        return "Fat Cardio Workout";
    }

    /**
     * Used to set the array of exercices
     */
    @Override
    protected void setExercises() {
        workouts = new String[]{
                getResources().getString(R.string.fat_ex_1_title),
                getResources().getString(R.string.fat_ex_2_title),
                getResources().getString(R.string.fat_ex_3_title),
                getResources().getString(R.string.fat_ex_4_title),
                getResources().getString(R.string.fat_ex_5_title),
                getResources().getString(R.string.fat_ex_6_title),
                getResources().getString(R.string.fat_ex_7_title),
                getResources().getString(R.string.fat_ex_8_title),
                getResources().getString(R.string.fat_ex_9_title),
                getResources().getString(R.string.fat_ex_10_title),
                " "};
    }

    /**
     * Sets the type of the workout
     *
     * @return type of the workout
     */
    @Override
    protected String setType() {
        return "FAT";
    }
}
