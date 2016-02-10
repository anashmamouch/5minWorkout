package com.benzino.fiveminworkout.instructions;

import com.benzino.fiveminworkout.data.Model;

/**
 * Created by Anas on 28/1/16.
 */
public class FatCardioInstruction extends InstructionActivity {

    @Override
    protected String setToolbarTitle() {
        return "Fat-burning Cardio";
    }

    @Override
    protected void addItems() {
        list.add(new Model("1. EXERCICE Fat ONE" , "this is a long description of exercice one bla bla bla yes that's right"));
        list.add(new Model("2. EXERCICE Fat TWO", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book bla bla bla yes that's right"));
        list.add(new Model("3. EXERCICE Fat THREE", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book this is a long description of exercice one bla bla bla yes that's right"));
        list.add(new Model("4. EXERCICE Fat FOUR", "this is a long description of exercice one bla bla bla yes that's right"));
        list.add(new Model("5. EXERCICE Fat FIVE", "this is a long description of exercice one bla bla bla yes that's right"));
        list.add(new Model("6. EXERCICE Fat SIX", "this is a long description of exercice one bla bla bla yes that's right"));
        list.add(new Model("7. EXERCICE Fat SEVEN", "this is a long description of exercice one bla bla bla yes that's right"));
        list.add(new Model("8. EXERCICE Fat EIGHT", "this is a long description of exercice one bla bla bla yes that's right"));
        list.add(new Model("9. EXERCICE Fat NINE", "this is a long description of exercice one bla bla bla yes that's right"));
        list.add(new Model("10. EXERCICE Fat TEN", "this is a long description of exercice one bla bla bla yes that's right"));

    }
}
