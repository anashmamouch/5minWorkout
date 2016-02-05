package com.benzino.fiveminworkout.instructions;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.benzino.fiveminworkout.Model;
import com.benzino.fiveminworkout.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anas on 28/1/16.
 */
public class PlyoCardio extends InstructionActivity {

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
    }

}
