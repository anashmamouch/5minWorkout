package com.benzino.fiveminworkout.workouts;

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
                "Abs 1",
                "Abs 2",
                "Abs 3",
                "Abs 4",
                "Abs 5",
                "Abs 6",
                "Abs 7",
                "Abs 8",
                "Abs 9",
                "Abs 10",
                " "};
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
