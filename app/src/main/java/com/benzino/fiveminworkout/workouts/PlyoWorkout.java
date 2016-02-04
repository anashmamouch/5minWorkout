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

    private static final int EXERCISE_TIME = 30000;
    private static final int REST_TIME = 10000;

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
        speak("5", TextToSpeech.QUEUE_ADD);
        silent();
        speak("4", TextToSpeech.QUEUE_ADD);
        silent();
        speak("3", TextToSpeech.QUEUE_ADD);
        silent();
        speak("2", TextToSpeech.QUEUE_ADD);
        silent();
        speak("1. "+message, TextToSpeech.QUEUE_ADD);
    }

    public void speaking(String[] array) {


        if(timeRemaining<111000 && 110500<=timeRemaining){
            speak(array[0], TextToSpeech.QUEUE_ADD);
        }

        if(timeRemaining <87000 && 86300<=timeRemaining){
            speakCountdown("Take a Rest. 10 seconds");
        }

        if (timeRemaining <77000 && 76300<=timeRemaining){
            speakCountdown(array[1]);
        }

        if (timeRemaining <47000 && 46300<=timeRemaining){
            speakCountdown("Take a Rest. 10 seconds.");
        }

        if (timeRemaining <37000 && 36300<=timeRemaining){
            speakCountdown(array[2]);
        }

        if (timeRemaining <7000 && 6300<=timeRemaining){
            speakCountdown("Take a rest and breathe deeply, your Workout is done for today.");
        }
    }

    public void textCounting(long millisUntilFinished){
        if(timeRemaining < 111000 && 81000 <= timeRemaining){
            counter.setText("" + ((millisUntilFinished - 81000) / 1000));
        }
        if(timeRemaining < 81000 && 71000 <= timeRemaining){
            counter.setText("" + ((millisUntilFinished - 71000) / 1000));
            exercise1.setText("REST");
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

    public void done() {
        counter.setText("");
        exercise2.setText("");
        exercise1.setText("THE END");
        db = DatabaseHandler.getInstance(PlyoWorkout.this);
        db.createTest(new Test("PLYO"));
    }

    public void startCounter(){
        //start.setText("PAUSE");
        pause.setEnabled(true);
        pause.setVisibility(View.VISIBLE);

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
                }else {

                    //Put the countdown remaining time in a variable
                    timeRemaining = millisUntilFinished;
                    //Display the remaining seconds to app interface
                    //1 second = 1000 milliseconds
                    counter.setText("" + (millisUntilFinished / 1000));

                    Log.d("ANASS", "isSpeaking?= " + tts.isSpeaking() + "; isReady?=" + ttsReady + ";  Time: " + timeRemaining);

                    String[] array ;
                    array = new String[]{
                            "Start Exercice 1",
                            "Start Exercice 2",
                            "Start Exercice 3"};

                    speaking(array);

                    textCounting(millisUntilFinished);

                }
            }
            /**
             * Callback fired when the time is up.
             */
            @Override
            public void onFinish() {
                   done();
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
                            "Start Exercice 1",
                            "Start Exercice 2",
                            "Start Exercice 3"};

                    speaking(array);

                    textCounting(millisUntilFinished);
                }
            }

            /**
             * Callback fired when the time is up.
             */
            @Override
            public void onFinish() {
                done();
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
