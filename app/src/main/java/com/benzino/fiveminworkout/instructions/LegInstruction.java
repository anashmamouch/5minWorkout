package com.benzino.fiveminworkout.instructions;

import com.benzino.fiveminworkout.Model;

/**
 * Created on 7/2/16.
 *
 * @author Anas
 */
public class LegInstruction extends InstructionActivity {
    @Override
    protected String setToolbarTitle() {
        return "Legs";
    }

    @Override
    protected void addItems() {

        list.add(new Model("1. EXERCICE LEG ONE" , "this is a long description of exercice one bla bla bla yes that's right"));
        list.add(new Model("2. EXERCICE LEG TWO", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book bla bla bla yes that's right"));
        list.add(new Model("3. EXERCICE LEG THREE", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book this is a long description of exercice one bla bla bla yes that's right"));
        list.add(new Model("4. EXERCICE LEG FOUR", "this is a long description of exercice one bla bla bla yes that's right"));
        list.add(new Model("5. EXERCICE LEG FIVE", "this is a long description of exercice one bla bla bla yes that's right"));
        list.add(new Model("6. EXERCICE LEG SIX", "this is a long description of exercice one bla bla bla yes that's right"));
        list.add(new Model("7. EXERCICE LEG SEVEN", "this is a long description of exercice one bla bla bla yes that's right"));
        list.add(new Model("8. EXERCICE LEG EIGHT", "this is a long description of exercice one bla bla bla yes that's right"));
        list.add(new Model("9. EXERCICE LEG NINE", "this is a long description of exercice one bla bla bla yes that's right"));
        list.add(new Model("10. EXERCICE LEG TEN", "this is a long description of exercice one bla bla bla yes that's right"));

    }
}