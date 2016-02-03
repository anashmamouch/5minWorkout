package com.benzino.fiveminworkout.workouts;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.benzino.fiveminworkout.DatabaseHandler;
import com.benzino.fiveminworkout.R;
import com.benzino.fiveminworkout.Test;

import java.util.Locale;

/**
 * Created by Anas on 31/1/16.
 */
public class PlyoWorkout extends AppCompatActivity {

    private TextView counter;
    private TextView exercise1;
    private TextView exercise2;
    private Button start;
    private Button resume;
    private Button pause;

    private long millisInFuture = 111000; //30 seconds
    private long interval = 1000; //1 second

    private boolean isPaused = false;
    private long timeRemaining = 0;

    private TextToSpeech tts;

    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plyoworkout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.workout_toolbar);
        toolbar.setTitle("Plyometric Cardio");

        Typeface font = Typeface.createFromAsset(getApplication().getAssets(), "fonts/Roboto-Thin.ttf");

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

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.setLanguage(Locale.US);
            }
        });


        counter = (TextView) findViewById(R.id.counter);
        exercise1 = (TextView) findViewById(R.id.first_exercice);
        exercise2 = (TextView) findViewById(R.id.second_exercice);

        start = (Button) findViewById(R.id.counter_button);
        pause = (Button) findViewById(R.id.counter_button_pause);
        resume = (Button) findViewById(R.id.counter_button_resume);

        //text.setText("23");
        //counter.setTextSize(75 * getResources().getDisplayMetrics().density);
        counter.setTypeface(font);

        pause.setEnabled(false);
        resume.setEnabled(false);
        pause.setVisibility(View.GONE);
        resume.setVisibility(View.GONE);

        counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter.setEnabled(false);
                startCounter();
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCounter();
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseCounter();
            }
        });

        resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resumeCounter();
            }
        });
    }

    @SuppressWarnings("deprecation")
    public void speak(String message){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
        }else{
            tts.speak(message, TextToSpeech.QUEUE_FLUSH, null);
        }
    }


    public void startCounter(){
        //start.setText("PAUSE");
        pause.setEnabled(true);
        start.setEnabled(false);
        start.setVisibility(View.GONE);
        pause.setVisibility(View.VISIBLE);

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
                }else{

                    //Put the countdown remaining time in a variable
                    timeRemaining = millisUntilFinished;
                    //Display the remaining seconds to app interface
                    //1 second = 1000 milliseconds
                    counter.setText("" + (millisUntilFinished/1000));

                    if(timeRemaining < 111000 && 81000 <= timeRemaining){
                        counter.setText("" + ((millisUntilFinished - 81000) / 1000));
                        //speak("Start exercise one ");
                    }
                    if(timeRemaining < 81000 && 71000 <= timeRemaining){
                        Log.d("ANAS", "I'm inside your IF Statement...");
                        counter.setText("" + ((millisUntilFinished - 71000)/ 1000));
                        exercise1.setText("REST");
                        //speak("REST");
                    }
                    if(timeRemaining < 71000 && 41000 <= timeRemaining){
                        exercise1.setText("EXERCICE TWO");
                        counter.setText("" + ((millisUntilFinished - 41000)/ 1000));
                        exercise2.setText("Next: EXERCICE THREE");
                    }
                    if(timeRemaining < 41000 && 31000 <=timeRemaining){
                        counter.setText("" + ((millisUntilFinished - 31000)/ 1000));
                        exercise1.setText("REST");
                    }
                    if(timeRemaining < 31000){
                        counter.setText(""+millisUntilFinished/1000);
                        exercise2.setText("");
                        exercise1.setText("EXERCISE THREE");
                    }
                }
            }
            /**
             * Callback fired when the time is up.
             */
            @Override
            public void onFinish() {
                    counter.setText("");
                    exercise2.setText("");
                    exercise1.setText("THE END");
                    db = DatabaseHandler.getInstance(PlyoWorkout.this);
                    db.createTest(new Test("PLYO"));
            }
        }.start();
    }

    public void resumeCounter(){
        resume.setEnabled(false);
        resume.setVisibility((View.GONE));
        pause.setEnabled(true);
        pause.setVisibility(View.VISIBLE);

        isPaused = false;

        //Initialize a new CountDownTimer instance
        final long millisInFuture = timeRemaining;

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
                }else{
                    counter.setText("" + ((millisUntilFinished)/1000));
                    //Put count down timer remaining time in a variable
                    timeRemaining = millisUntilFinished;
                }
            }

            /**
             * Callback fired when the time is up.
             */
            @Override
            public void onFinish() {

            }
        }.start();
    }

    public void pauseCounter(){
        isPaused = true;

        pause.setEnabled(false);
        pause.setVisibility(View.GONE);
        resume.setEnabled(true);
        resume.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(tts != null){
            tts.stop();
            tts.shutdown();
        }
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
