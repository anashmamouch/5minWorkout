package com.benzino.fiveminworkout.workouts;

/**
 * Created on 7/2/16.
 *
 * @author Anas
 */
public class LegWorkout extends WorkoutActivity {
    /**
     * Used to set the title of the toolbar
     *
     * @return the title of the toolbar
     */
    @Override
    protected String setToolbarTitle() {

        return "Leg Workout";
    }

    /**
     * Used to set the array of exercices
     */
    @Override
    protected void setExercises() {
        workouts = new String[]{
                "Leg 1",
                "Leg 2",
                "Leg 3",
                "Leg 4",
                "Leg 5",
                "Leg 6",
                "Leg 7",
                "Leg 8",
                "Leg 9",
                "Leg 10",
                " "};
    }

    /**
     * Sets the type of the workout
     *
     * @return type of the workout
     */
    @Override
    protected String setType() {
        return "LEG";
    }
}
