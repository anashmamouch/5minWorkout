package com.benzino.fiveminworkout.workouts;

/**
 *
 * Created on 7/2/16.
 *
 * @author Anas
 */
public class PlyoCardioWorkout extends WorkoutActivity{

    /**
     * Used to set the title of the toolbar
     *
     * @return the title of the toolbar
     */
    @Override
    protected String setToolbarTitle() {
        return "Plyometric Cardio";
    }

    /**
     * Used to set the array of exercices
     */
    @Override
    protected void setExercises() {
        workouts = new String[]{
                "Plyometric 1",
                "Plyometric 2",
                "Plyometric 3",
                "Plyometric 4",
                "Plyometric 5",
                "Plyometric 6",
                "Plyometric 7",
                "Plyometric 8",
                "Plyometric 9",
                "Plyometric 10",
                " "};
    }

    /**
     * Sets the type of the workout
     *
     * @return type of the workout
     */
    @Override
    protected String setType() {
        return "PLYO";
    }
}
