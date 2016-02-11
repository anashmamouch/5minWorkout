package com.benzino.fiveminworkout.instructions;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.benzino.fiveminworkout.data.Model;
import com.benzino.fiveminworkout.R;
import com.benzino.fiveminworkout.activities.SettingsActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anas on 31/1/16.
 */
public abstract class InstructionActivity extends AppCompatActivity {

    protected List<Model> list;
    AdView mAdView;

    int anas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        anas = sp.getInt("preference_number", 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.instruction_toolbar);
        toolbar.setTitle(setToolbarTitle());

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.instruction_recyclerView);
        recyclerView.setHasFixedSize(true);

        list = new ArrayList<Model>();

        addItems();

        CustomAdapter adapter = new CustomAdapter(list, InstructionActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(InstructionActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }
    /** Called when leaving the activity */
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    protected abstract String setToolbarTitle();

    protected abstract void addItems();


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            return true;
        }
        else if(id == R.id.action_share) {
            String link = "http://play.google.com/store/apps/details?id=com.benzino.fiveminworkout";
            String message = "Check out this awesome app i just installed, it helps me lose fat and improve my health.\nDownload it from here: \n" + link;
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(Intent.createChooser(share, "Share 5 minute Workout"));

            return true;
        }
        else if(id == R.id.action_feedback){
            Intent Email = new Intent(Intent.ACTION_SEND);
            Email.setType("text/email");
            Email.putExtra(Intent.EXTRA_EMAIL, new String[] { "benzinoanas@gmail.com" });
            Email.putExtra(Intent.EXTRA_SUBJECT, "Feedback 5min Workout app");
            Email.putExtra(Intent.EXTRA_TEXT, "Dear Developer," + "\n");
            startActivity(Intent.createChooser(Email, "Send Feedback:"));

            return true;
        }else if(id == R.id.action_rate){
            Uri uri = Uri.parse("market://details?id=com.benzino.fiveminworkout");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=com.benzino.fiveminworkout")));
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
