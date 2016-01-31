package com.benzino.fiveminworkout.workouts;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.benzino.fiveminworkout.R;

/**
 * Created by Anas on 31/1/16.
 */
public class PlyoWorkout extends AppCompatActivity {

    private boolean isPaused = false;
    private long timeRemaining = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plyoworkout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.workout_toolbar);
        toolbar.setTitle("Plyometric Cardio Workout");

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

        final TextView counter = (TextView) findViewById(R.id.counter);
        //text.setText("23");
        counter.setTextSize(62 * getResources().getDisplayMetrics().density);

        final Button start = (Button) findViewById(R.id.counter_button);
        final Button pause = (Button) findViewById(R.id.counter_button_pause);

        pause.setEnabled(false);
        

        final long millisInFuture = 30000; //30 seconds
        final long interval = 1000; // 1 second

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setText("PAUSE");
                new CountDownTimer(millisInFuture, interval) {

                    /**
                     * Callback fired on regular interval.
                     *
                     * @param millisUntilFinished The amount of time until finished.
                     */
                    @Override
                    public void onTick(long millisUntilFinished) {
                        if(isPaused){

                            cancel();
                        }
                        //Put the countdown remaining time in a variable
                        timeRemaining = millisUntilFinished;
                        //Display the remaining seconds to app interface
                        //1 second = 1000 milliseconds
                        counter.setText(" " + ((millisInFuture - millisUntilFinished + 1000)/1000));
                    }

                    /**
                     * Callback fired when the time is up.
                     */
                    @Override
                    public void onFinish() {

                    }
                }.start();
            }
        });
    }

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
