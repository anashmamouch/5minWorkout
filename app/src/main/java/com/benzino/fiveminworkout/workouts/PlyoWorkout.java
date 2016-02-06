package com.benzino.fiveminworkout.workouts;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PowerManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.benzino.fiveminworkout.DatabaseHandler;
import com.benzino.fiveminworkout.R;
import com.benzino.fiveminworkout.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Anas on 31/1/16.
 */
public class PlyoWorkout extends AppCompatActivity implements View.OnClickListener{

    private TextView counter;
    private TextView exercise1;
    private TextView exercise2;
    private ImageView video;
    private Button repeatWorkout;
    private Button changeWorkout;
    private Button share;
    private Button resume;
    private Button pause;
    private ProgressBar spinner;

    private long TOTAL_TIME = 300000; //5 minutes Workout
    private long EXERCISE_TIME = 7000; // 20seconds exercice time
    private long INTERVAL = 1000; // 1second interval
    private long REST_TIME = 7000; // 10seconds rest time

    private int n = 1;//variables used to change textView counter
    private int j = 1;

    private boolean isPaused = false;//boolean used to pause the counter

    private CountDownTimer timer;//countdown timer used to calculate time remaining for the workout

    private long timeRemaining = 0;//variable to store the remaining time and use it to resume timer

    private TextToSpeech tts;//text to speech allows speaking while counting

    private volatile boolean ttsReady = false;//waits for text to speach to initialize

    private String[] workouts;

    private long startTime;
    private long launchTime;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plyoworkout);
        //Keeping the device awake during the workout
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        mWakeLock.acquire();


        Toolbar toolbar = (Toolbar) findViewById(R.id.workout_toolbar);
        toolbar.setTitle("Plyometric Cardio");

        Typeface font = Typeface.createFromAsset(getApplication().getAssets(), "fonts/Roboto-Thin.ttf");

        toolbar.setNavigationIcon(R.mipmap.ic_arrow_left_white_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(final int status) {
              tts.setLanguage(Locale.US);
            }
        });

        counter = (TextView) findViewById(R.id.counter);
        counter.setTypeface(font);

        exercise1 = (TextView) findViewById(R.id.first_exercice);

        exercise2 = (TextView) findViewById(R.id.second_exercice);

        video = (ImageView) findViewById(R.id.video_icon);
        video.setVisibility(View.GONE);

        repeatWorkout = (Button) findViewById(R.id.repeat_workout);
        repeatWorkout.setVisibility(View.GONE);

        changeWorkout = (Button) findViewById(R.id.change_workout);
        changeWorkout.setVisibility(View.GONE);

        share = (Button) findViewById(R.id.share_workout);
        share.setVisibility(View.GONE);

        pause = (Button) findViewById(R.id.counter_button_pause);
        pause.setEnabled(false);
        pause.setVisibility(View.GONE);

        resume = (Button) findViewById(R.id.counter_button_resume);
        resume.setEnabled(false);
        resume.setVisibility(View.GONE);

        spinner = (ProgressBar) findViewById(R.id.spinner);
        spinner.setVisibility(View.GONE);

        workouts = new String[]{
                "Exercise 1",
                "Exercise 2",
                "Exercise 3",
                "Exercise 4",
                "Exercise 5",
                "Exercise 6",
                "Exercise 7",
                "Exercise 8",
                "Exercise 9",
                "Exercise 10",
                " "};

        exercise1.setText(workouts[0]);
        exercise2.setText("Next: " + workouts[1]);

        counter.setOnClickListener(this);
        pause.setOnClickListener(this);
        resume.setOnClickListener(this);
        repeatWorkout.setOnClickListener(this);
        changeWorkout.setOnClickListener(this);
        share.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.counter){
            counter.setEnabled(false);
            launchTime = System.currentTimeMillis();

            initializeTextToSpeech();

            new Task().execute();
        }
        if (view.getId() == R.id.counter_button_pause)
            pauseCounter();

        if (view.getId() == R.id.counter_button_resume)
            resumeCounter(workouts);

        if(view.getId() == R.id.repeat_workout){
            finish();
            startActivity(new Intent(PlyoWorkout.this, PlyoWorkout.class));
        }
        if (view.getId() == R.id.change_workout)
            onBackPressed();

        if (view.getId() == R.id.share_workout)
            Toast.makeText(this, "SHARE ME ", Toast.LENGTH_SHORT).show();
    }

    public void initializeTextToSpeech(){
        Bundle bundle = new Bundle();
        bundle.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "ANAS");

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "ANAS");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            tts.speak(" ", TextToSpeech.QUEUE_FLUSH, bundle, "ANAS");
        }else{
            tts.speak(" ", TextToSpeech.QUEUE_FLUSH, hashMap);
        }

        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                startTime = System.currentTimeMillis();
                ttsReady = true;
                Log.d("ANAS", "started: " + startTime + ", delay: " + (startTime - launchTime));
            }

            @Override
            public void onDone(String utteranceId) {
                long millis = System.currentTimeMillis();
                Log.d("ANAS", "done: " + millis + ", total: " + (millis - launchTime) + ", duration: " + (millis - startTime));
            }

            @Override
            public void onError(String utteranceId) {

            }
        });
    }

    @SuppressWarnings("deprecation")
    public void speak(String message, int queue ){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(message, queue, null, null);
            //tts.stop();
        }else{
            tts.speak(message, queue, null);
        }
    }

    @SuppressWarnings("deprecation")
    public void silent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.playSilentUtterance(400, TextToSpeech.QUEUE_ADD, null);
        }else {
            tts.playSilence(400, TextToSpeech.QUEUE_ADD, null);
        }
    }

    public void speakCountdown(String message){
        speak("3", TextToSpeech.QUEUE_ADD);
        silent();
        speak("2", TextToSpeech.QUEUE_ADD);
        silent();
        speak("1. "+message, TextToSpeech.QUEUE_ADD);
    }

    public void speaking(String[] array) {

        if(timeRemaining < (TOTAL_TIME) && (TOTAL_TIME - 400) <=timeRemaining){
            speak("Start " + array[0], TextToSpeech.QUEUE_ADD);
        }
        if(timeRemaining <(TOTAL_TIME - n * EXERCISE_TIME -  (n - 1) * REST_TIME - INTERVAL + 4000)
                && (TOTAL_TIME - n * EXERCISE_TIME - (n - 1) * REST_TIME - INTERVAL + 3700)<=timeRemaining && REST_TIME !=0){

            if (n == (array.length - 1)){
                speakCountdown("Your workout is done for today");
            }else{
                speakCountdown("Take a Rest. " + (REST_TIME / 1000) + " seconds");
            }
        }
        if(timeRemaining <(TOTAL_TIME - n * EXERCISE_TIME -  n * REST_TIME - INTERVAL+ 4000)
                && (TOTAL_TIME - n * EXERCISE_TIME - n * REST_TIME - INTERVAL + 3700)<=timeRemaining ){
            speakCountdown("Start " + array[n]);
            n++ ;
        }
    }

    public void textCounting(long millisUntilFinished, String[] array) {
        if (timeRemaining < TOTAL_TIME
                && (TOTAL_TIME - EXERCISE_TIME) <= timeRemaining) {
            counter.setText("" + ((millisUntilFinished - (TOTAL_TIME - EXERCISE_TIME - INTERVAL)) / INTERVAL));
            exercise1.setText(array[0]);
            exercise2.setText("Next: "+ array[1]);
            video.setVisibility(View.VISIBLE);
        }
        if (timeRemaining < (TOTAL_TIME - j * EXERCISE_TIME - (j - 1) * REST_TIME)
                && (TOTAL_TIME - j * EXERCISE_TIME - j * REST_TIME) <= timeRemaining) {
            counter.setText("" + ((millisUntilFinished - (TOTAL_TIME - j * EXERCISE_TIME - j * REST_TIME - INTERVAL)) / INTERVAL ));
            exercise1.setText("REST");
            video.setVisibility(View.GONE);
        }
        if (timeRemaining < (TOTAL_TIME - j * EXERCISE_TIME - j * REST_TIME)
                && (TOTAL_TIME - (j + 1) * EXERCISE_TIME - j * REST_TIME) <= timeRemaining) {

            counter.setText("" + ((millisUntilFinished - (TOTAL_TIME - (j+ 1) * EXERCISE_TIME - j * REST_TIME - INTERVAL)) / INTERVAL));
            exercise1.setText(array[j]);
            video.setVisibility(View.VISIBLE);

            if (j == (array.length - 2) ){
                counter.setText("" + ((millisUntilFinished - (TOTAL_TIME - (j+ 1) * EXERCISE_TIME - j * REST_TIME)) / INTERVAL));
                exercise2.setText("");
            }else {
                exercise2.setText("Next: " + array[j + 1]);
            }
        }

        if ( timeRemaining < (TOTAL_TIME - (j + 1) * EXERCISE_TIME - j * REST_TIME + INTERVAL)){
            j++;
            Log.d("ANASTORM", "Array = "+array[j]+"; Value of j = " + j +"; Value of n = "+ n );
        }
    }

    public void done(String message) {
        Typeface font = Typeface.createFromAsset(getApplication().getAssets(), "fonts/lobster.ttf");

        DatabaseHandler.getInstance(PlyoWorkout.this).createTest(new Test(message));

        j = 1;
        n = 1;

        exercise1.setTypeface(font);
        exercise1.setText("Congragulations");
        counter.setText("");
        exercise2.setText("");

        video.setVisibility(View.GONE);
        pause.setVisibility(View.GONE);
        resume.setVisibility(View.GONE);

        repeatWorkout.setVisibility(View.VISIBLE);
        changeWorkout.setVisibility(View.VISIBLE);
        share.setVisibility(View.VISIBLE);


    }

    public void startCounter(final String[] array){
        //start.setText("PAUSE");
        pause.setEnabled(true);
        pause.setVisibility(View.VISIBLE);

        TOTAL_TIME = 10 * EXERCISE_TIME + 9 * REST_TIME;

        timer = new CountDownTimer(TOTAL_TIME, INTERVAL) {

            /**
             * Callback fired on regular interval.
             *
             * @param millisUntilFinished The amount of time until finished.
             */
            @Override
            public void onTick(long millisUntilFinished) {
                if(isPaused){

                    cancel();
                }else {

                    //Put the countdown remaining time in a variable
                    timeRemaining = millisUntilFinished;
                    //Display the remaining seconds to app interface
                    //1 second = 1000 milliseconds
                    counter.setText("" + (millisUntilFinished / 1000));

                    Log.d("ANASS", "isSpeaking?= " + tts.isSpeaking() + "; isReady?=" + ttsReady + ";  Time: " + timeRemaining);

                    speaking(array);

                    textCounting(millisUntilFinished, array);

                }
            }
            /**
             * Callback fired when the time is up.
             */
            @Override
            public void onFinish() {
                   done("PLYO");
            }
        }.start();

    }

    public void resumeCounter(final String[] array){
        resume.setEnabled(false);
        resume.setVisibility((View.GONE));
        pause.setEnabled(true);
        pause.setVisibility(View.VISIBLE);

        isPaused = false;


        //Initialize a new CountDownTimer instance
        final long millisInFuture = timeRemaining;

        timer = new CountDownTimer(millisInFuture, INTERVAL) {
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


                    speaking(array);

                    textCounting(millisUntilFinished, array);
                }
            }

            /**
             * Callback fired when the time is up.
             */
            @Override
            public void onFinish() {
                done("PLYO");
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
        if(timer !=null){
            timer.cancel();
            timer = null;
        }
    }



    public class Task extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            spinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            while(!ttsReady){

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            spinner.setVisibility(View.GONE);
            startCounter(workouts);
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
