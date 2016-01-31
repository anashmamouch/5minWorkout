package com.benzino.fiveminworkout.instructions;

import com.benzino.fiveminworkout.Model;

/**
 * Created by Anas on 28/1/16.
 */
public class FatCardio extends InstructionActivity {

    @Override
    protected String setToolbarTitle() {
        return "Fat-burning Cardio";
    }

    @Override
    protected void addItems() {
        list.add(new Model("1. EXERCICE FAT ONE", "this is a long description of exercice one bla bla bla yes that's right"));
        list.add(new Model("2. EXERCICE FAT TWO", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book bla bla bla yes that's right"));
        list.add(new Model("3. EXERCICE FAT THREE", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book this is a long description of exercice one bla bla bla yes that's right"));
        list.add(new Model("4. EXERCICE FAT FOUR", "this is a long description of exercice one bla bla bla yes that's right"));
        list.add(new Model("5. EXERCICE FAT FIVE", "this is a long description of exercice one bla bla bla yes that's right"));

    }
}
