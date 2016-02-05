package com.benzino.fiveminworkout.workouts;

import android.annotation.TargetApi;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
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
import android.widget.ProgressBar;
import android.widget.TextView;

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
public class PlyoWorkout extends AppCompatActivity {

    private TextView counter;
    private TextView exercise1;
    private TextView exercise2;
    private Button resume;
    private Button pause;
    private ProgressBar spinner;

    private long millisInFuture = 111000; //30 seconds
    private long interval = 1000; //1 second

    private long TOTAL_TIME = 300000; //5 minutes Workout
    private long EXERCISE_TIME = 13000; // 20seconds exercice time
    private long INTERVAL = 1000; // 1second interval
    private long REST_TIME = 7000; // 10seconds rest time

    private int n = 1;
    private int j = 1;

    private boolean isPaused = false;

    private CountDownTimer timer;

    private long timeRemaining = 0;

    private TextToSpeech tts;

    private volatile boolean ttsReady = false;

    private DatabaseHandler db;

    private long startTime;
    private long launchTime;


    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plyoworkout);

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
                counter.setEnabled(status == TextToSpeech.SUCCESS);
            }
        });


        counter = (TextView) findViewById(R.id.counter);
        exercise1 = (TextView) findViewById(R.id.first_exercice);
        exercise2 = (TextView) findViewById(R.id.second_exercice);


        pause = (Button) findViewById(R.id.counter_button_pause);
        resume = (Button) findViewById(R.id.counter_button_resume);

        spinner = (ProgressBar) findViewById(R.id.spinner);
        spinner.setVisibility(View.GONE);

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
                launchTime = System.currentTimeMillis();

                initializeTextToSpeech();

                //startCounter();

                new Task().execute();
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
//        speak("5", TextToSpeech.QUEUE_ADD);
//        silent();
//        speak("4", TextToSpeech.QUEUE_ADD);
//        silent();
        speak("3", TextToSpeech.QUEUE_ADD);
        silent();
        speak("2", TextToSpeech.QUEUE_ADD);
        silent();
        speak("1. "+message, TextToSpeech.QUEUE_ADD);
    }

    public void speaking(String[] array) {

        if(timeRemaining <(TOTAL_TIME) && (TOTAL_TIME - 400)<=timeRemaining){
            speak("Start " + array[0], TextToSpeech.QUEUE_ADD);
        }
        if(timeRemaining <(TOTAL_TIME - n * EXERCISE_TIME -  (n - 1) * REST_TIME +4000)
                && (TOTAL_TIME - n * EXERCISE_TIME - (n - 1) * REST_TIME +3700)<=timeRemaining && REST_TIME !=0){
            speakCountdown("Take a Rest. " + (REST_TIME/1000) + " seconds");
        }
        if(timeRemaining <(TOTAL_TIME - n * EXERCISE_TIME -  n * REST_TIME +4000)
                && (TOTAL_TIME - n * EXERCISE_TIME - n * REST_TIME +3700)<=timeRemaining && REST_TIME !=0){
            speakCountdown("Start "+ array[n]);
            n++ ;
        }
    }

    public void textCounting(long millisUntilFinished, String[] array) {
        if (timeRemaining < TOTAL_TIME
                && (TOTAL_TIME - EXERCISE_TIME) <= timeRemaining) {
            counter.setText("" + ((millisUntilFinished - (TOTAL_TIME - EXERCISE_TIME)) / INTERVAL));
        }
        if (timeRemaining < (TOTAL_TIME - j * EXERCISE_TIME - (j - 1) * REST_TIME)
                && (TOTAL_TIME - j * EXERCISE_TIME - j * REST_TIME) <= timeRemaining) {
            counter.setText("" + ((millisUntilFinished - (TOTAL_TIME - j * EXERCISE_TIME - j * REST_TIME)) / INTERVAL ));
            exercise1.setText("REST");
        }
        if (timeRemaining < (TOTAL_TIME - j * EXERCISE_TIME - j * REST_TIME)
                && (TOTAL_TIME - (j + 1) * EXERCISE_TIME - j * REST_TIME) <= timeRemaining) {

            counter.setText("" + ((millisUntilFinished - (TOTAL_TIME - (j+ 1) * EXERCISE_TIME - j * REST_TIME)) / INTERVAL));
            exercise1.setText(array[j]);
            exercise2.setText("Next: "+ array[j + 1]);
        }

        if ( timeRemaining <= (TOTAL_TIME - (j + 1) * EXERCISE_TIME - j * REST_TIME)){
            j++;
        }
        /**
        if (timeRemaining < (TOTAL_TIME - EXERCISE_TIME - REST_TIME)
                && (TOTAL_TIME - 2 * EXERCISE_TIME - REST_TIME) <= timeRemaining) {

            counter.setText("" + ((millisUntilFinished - (TOTAL_TIME - 2 * EXERCISE_TIME - REST_TIME)) / INTERVAL));
            exercise1.setText("EXERCISE TWO");
            exercise2.setText("Next: EXERCISE THREE");
        }
        if (timeRemaining < (TOTAL_TIME - 2 * EXERCISE_TIME - REST_TIME)
                && (TOTAL_TIME - 2 * EXERCISE_TIME - 2 * REST_TIME) <= timeRemaining) {
            counter.setText("" + ((millisUntilFinished - (TOTAL_TIME - 2 * EXERCISE_TIME - 2 * REST_TIME)) / INTERVAL));
            exercise1.setText("REST");
        }
        if (timeRemaining < (TOTAL_TIME - 2 * EXERCISE_TIME - 2 * REST_TIME)
                && (TOTAL_TIME - 3 * EXERCISE_TIME - 2 * REST_TIME) <= timeRemaining) {
            counter.setText("" + (millisUntilFinished - (TOTAL_TIME - 3 * EXERCISE_TIME - 2 * REST_TIME)) / INTERVAL);
            exercise1.setText("EXERCISE THREE");
            exercise2.setText("Next: EXERCISE FOUR");
        }
        if (timeRemaining < (TOTAL_TIME - 3 * EXERCISE_TIME - 2 * REST_TIME)
                && (TOTAL_TIME - 3 * EXERCISE_TIME - 3 * REST_TIME) <= timeRemaining) {
            counter.setText("" + (millisUntilFinished - (TOTAL_TIME - 3 * EXERCISE_TIME - 3 * REST_TIME)) / INTERVAL);
            exercise1.setText("REST");
        }
        if (timeRemaining < (TOTAL_TIME - 3 * EXERCISE_TIME - 3 * REST_TIME)
                && (TOTAL_TIME - 4 * EXERCISE_TIME - 3 * REST_TIME) <= timeRemaining) {
            counter.setText("" + (millisUntilFinished - (TOTAL_TIME - 4 * EXERCISE_TIME - 3 * REST_TIME)) / INTERVAL);
            exercise1.setText("EXERCISE FOUR");
            exercise2.setText("Next: EXERCISE FIVE");
        }
        if (timeRemaining < (TOTAL_TIME - 4 * EXERCISE_TIME - 3 * REST_TIME)
                && (TOTAL_TIME - 4 * EXERCISE_TIME - 4 * REST_TIME) <= timeRemaining) {
            counter.setText("" + (millisUntilFinished - (TOTAL_TIME - 4 * EXERCISE_TIME - 4 * REST_TIME)) / INTERVAL);
            exercise1.setText("REST");
        }
        if (timeRemaining < (TOTAL_TIME - 4 * EXERCISE_TIME - 4 * REST_TIME)
                && (TOTAL_TIME - 5 * EXERCISE_TIME - 4 * REST_TIME) <= timeRemaining) {
            counter.setText("" + (millisUntilFinished - (TOTAL_TIME - 5 * EXERCISE_TIME - 4 * REST_TIME)) / INTERVAL);
            exercise1.setText("EXERCISE FIVE");
            exercise2.setText("Next: EXERCISE SIX");
        }
        if (timeRemaining < (TOTAL_TIME - 5 * EXERCISE_TIME - 4 * REST_TIME)
                && (TOTAL_TIME - 5 * EXERCISE_TIME - 5 * REST_TIME) <= timeRemaining) {
            counter.setText("" + (millisUntilFinished - (TOTAL_TIME - 5 * EXERCISE_TIME - 5 * REST_TIME)) / INTERVAL);
            exercise1.setText("REST");
        }
        if (timeRemaining < (TOTAL_TIME - 5 * EXERCISE_TIME - 5 * REST_TIME)
                && (TOTAL_TIME - 6 * EXERCISE_TIME - 5 * REST_TIME) <= timeRemaining) {
            counter.setText("" + (millisUntilFinished - (TOTAL_TIME - 6 * EXERCISE_TIME - 5 * REST_TIME)) / INTERVAL);
            exercise1.setText("EXERCISE SIX");
            exercise2.setText("Next: EXERCISE SEVEN");
        }
        if (timeRemaining < (TOTAL_TIME - 6 * EXERCISE_TIME - 5 * REST_TIME)
                && (TOTAL_TIME - 6 * EXERCISE_TIME - 6 * REST_TIME) <= timeRemaining) {
            counter.setText("" + (millisUntilFinished - (TOTAL_TIME - 6 * EXERCISE_TIME - 6 * REST_TIME)) / INTERVAL);
            exercise1.setText("REST");
        }
        if (timeRemaining < (TOTAL_TIME - 6 * EXERCISE_TIME - 6 * REST_TIME)
                && (TOTAL_TIME - 7 * EXERCISE_TIME - 6 * REST_TIME) <= timeRemaining) {
            counter.setText("" + (millisUntilFinished - (TOTAL_TIME - 7 * EXERCISE_TIME - 6 * REST_TIME)) / INTERVAL);
            exercise1.setText("EXERCISE SEVEN");
            exercise2.setText("Next: EXERCISE EIGHT");
        }
        if (timeRemaining < (TOTAL_TIME - 7 * EXERCISE_TIME - 6 * REST_TIME)
                && (TOTAL_TIME - 7 * EXERCISE_TIME - 7 * REST_TIME) <= timeRemaining) {
            counter.setText("" + (millisUntilFinished - (TOTAL_TIME - 7 * EXERCISE_TIME - 7 * REST_TIME)) / INTERVAL);
            exercise1.setText("REST");
        }
        if (timeRemaining < (TOTAL_TIME - 7 * EXERCISE_TIME - 7 * REST_TIME)
                && (TOTAL_TIME - 8 * EXERCISE_TIME - 7 * REST_TIME) <= timeRemaining) {
            counter.setText("" + (millisUntilFinished - (TOTAL_TIME - 8 * EXERCISE_TIME - 7 * REST_TIME)) / INTERVAL);
            exercise1.setText("EXERCISE EIGHT");
            exercise2.setText("Next: EXERCISE NINE");
        }
        if (timeRemaining < (TOTAL_TIME - 8 * EXERCISE_TIME - 7 * REST_TIME)
                && (TOTAL_TIME - 8 * EXERCISE_TIME - 8 * REST_TIME) <= timeRemaining) {
            counter.setText("" + (millisUntilFinished - (TOTAL_TIME - 8 * EXERCISE_TIME - 8 * REST_TIME)) / INTERVAL);
            exercise1.setText("REST");
        }
        if (timeRemaining < (TOTAL_TIME - 8 * EXERCISE_TIME - 8 * REST_TIME)
                && (TOTAL_TIME - 9 * EXERCISE_TIME - 8 * REST_TIME) <= timeRemaining) {
            counter.setText("" + (millisUntilFinished - (TOTAL_TIME - 9 * EXERCISE_TIME - 8 * REST_TIME)) / INTERVAL);
            exercise1.setText("EXERCISE NINE");
            exercise2.setText("Next: EXERCISE TEN");
        }
        if (timeRemaining < (TOTAL_TIME - 9 * EXERCISE_TIME - 8 * REST_TIME)
                && (TOTAL_TIME - 9 * EXERCISE_TIME - 9 * REST_TIME) <= timeRemaining) {
            counter.setText("" + (millisUntilFinished - (TOTAL_TIME - 9 * EXERCISE_TIME - 9 * REST_TIME)) / INTERVAL);
            exercise1.setText("REST");
        }
        if (timeRemaining < (TOTAL_TIME - 9 * EXERCISE_TIME - 9 * REST_TIME)
                && (TOTAL_TIME - 10 * EXERCISE_TIME - 9 * REST_TIME) <= timeRemaining) {
            counter.setText("" + (millisUntilFinished - (TOTAL_TIME - 10 * EXERCISE_TIME - 9 * REST_TIME)) / INTERVAL);
            exercise1.setText("EXERCICE TEN");
            exercise2.setText(" ");
        }
         **/
    }

    public void done(String message) {

        counter.setText("");
        exercise2.setText("");
        exercise1.setText("THE END");
        db = DatabaseHandler.getInstance(PlyoWorkout.this);
        db.createTest(new Test(message));
    }

    public void startCounter(){
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

                    String[] array ;
                    array = new String[]{
                            "Exercise 1",
                            "Exercise 2",
                            "Exercise 3",
                            "Exercise 4",
                            "Exercise 5",
                            "Exercise 6",
                            "Exercise 7",
                            "Exercise 8",
                            "Exercise 9"};

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

    public void resumeCounter(){
        resume.setEnabled(false);
        resume.setVisibility((View.GONE));
        pause.setEnabled(true);
        pause.setVisibility(View.VISIBLE);

        isPaused = false;


        //Initialize a new CountDownTimer instance
        final long millisInFuture = timeRemaining;

        timer = new CountDownTimer(millisInFuture, interval) {
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

                    String[] array ;
                    array = new String[]{
                            "Exercise 1",
                            "Exercise 2",
                            "Exercise 3",
                            "Exercise 4",
                            "Exercise 5",
                            "Exercise 6",
                            "Exercise 7",
                            "Exercise 8",
                            "Exercise 9",
                            "Exercice 10"};

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
            startCounter();
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
