package com.benzino.fiveminworkout.workouts;

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
                "Fat 1",
                "Fat 2",
                "Fat 3",
                "Fat 4",
                "Fat 5",
                "Fat 6",
                "Fat 7",
                "Fat 8",
                "Fat 9",
                "Fat 10",
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
