package com.benzino.fiveminworkout.instructions;

import com.benzino.fiveminworkout.data.Model;

/**
 * Created by Anas on 28/1/16.
 */
public class PlyoCardioInstruction extends InstructionActivity {

    @Override
    protected String setToolbarTitle() {
        return "Plyometric Cardio";
    }

    @Override
    protected void addItems() {
        list.add(new Model("1. EXERCICE ONE" , "this is a long description of exercice one bla bla bla yes that's right"));
        list.add(new Model("2. EXERCICE TWO", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book bla bla bla yes that's right"));
        list.add(new Model("3. EXERCICE THREE", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book this is a long description of exercice one bla bla bla yes that's right"));
        list.add(new Model("4. EXERCICE FOUR", "this is a long description of exercice one bla bla bla yes that's right"));
        list.add(new Model("5. EXERCICE FIVE", "this is a long description of exercice one bla bla bla yes that's right"));
        list.add(new Model("6. EXERCICE SIX", "this is a long description of exercice one bla bla bla yes that's right"));
        list.add(new Model("7. EXERCICE SEVEN", "this is a long description of exercice one bla bla bla yes that's right"));
        list.add(new Model("8. EXERCICE EIGHT", "this is a long description of exercice one bla bla bla yes that's right"));
        list.add(new Model("9. EXERCICE NINE", "this is a long description of exercice one bla bla bla yes that's right"));
        list.add(new Model("10. EXERCICE TEN", "this is a long description of exercice one bla bla bla yes that's right"));
    }

}
