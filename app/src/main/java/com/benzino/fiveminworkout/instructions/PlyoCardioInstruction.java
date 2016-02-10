package com.benzino.fiveminworkout.instructions;

import com.benzino.fiveminworkout.R;
import com.benzino.fiveminworkout.data.Model;

/**
 * Created by Anas on 28/1/16.
 */
public class PlyoCardioInstruction extends InstructionActivity {

    @Override
    protected String setToolbarTitle() {
        return getResources().getString(R.string.plyometric_title);
    }

    @Override
    protected void addItems() {
        list.add(new Model("1. "+getResources().getString(R.string.plyo_ex_1_title) ,getResources().getString(R.string.plyo_ex_1_desc), getResources().getString(R.string.plyo_ex_1_url)));
        list.add(new Model("2. "+getResources().getString(R.string.plyo_ex_2_title) ,getResources().getString(R.string.plyo_ex_2_desc), getResources().getString(R.string.plyo_ex_2_url)));
        list.add(new Model("3. "+getResources().getString(R.string.plyo_ex_3_title) ,getResources().getString(R.string.plyo_ex_3_desc), getResources().getString(R.string.plyo_ex_3_url)));
        list.add(new Model("4. "+getResources().getString(R.string.plyo_ex_4_title) ,getResources().getString(R.string.plyo_ex_4_desc), getResources().getString(R.string.plyo_ex_4_url)));
        list.add(new Model("5. "+getResources().getString(R.string.plyo_ex_5_title) ,getResources().getString(R.string.plyo_ex_5_desc), getResources().getString(R.string.plyo_ex_5_url)));
        list.add(new Model("6. "+getResources().getString(R.string.plyo_ex_6_title) ,getResources().getString(R.string.plyo_ex_6_desc), getResources().getString(R.string.plyo_ex_6_url)));
        list.add(new Model("7. "+getResources().getString(R.string.plyo_ex_7_title) ,getResources().getString(R.string.plyo_ex_7_desc), getResources().getString(R.string.plyo_ex_7_url)));
        list.add(new Model("8. "+getResources().getString(R.string.plyo_ex_8_title) ,getResources().getString(R.string.plyo_ex_8_desc), getResources().getString(R.string.plyo_ex_8_url)));
        list.add(new Model("9. "+getResources().getString(R.string.plyo_ex_9_title) ,getResources().getString(R.string.plyo_ex_9_desc), getResources().getString(R.string.plyo_ex_9_url)));
        list.add(new Model("10. "+getResources().getString(R.string.plyo_ex_10_title) ,getResources().getString(R.string.plyo_ex_10_desc), getResources().getString(R.string.plyo_ex_10_url)));

    }

}
