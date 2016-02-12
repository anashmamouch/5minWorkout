package com.benzino.fiveminworkout.workouts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.benzino.fiveminworkout.data.DatabaseHandler;
import com.benzino.fiveminworkout.helpers.MyCountDownTimer;
import com.benzino.fiveminworkout.R;
import com.benzino.fiveminworkout.data.Test;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created on 31/1/16.
 * @author Anas
 */
public abstract class WorkoutActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView counter;
    private TextView exercise1;
    private TextView exercise2;
    private ImageView video;
    private TextView congratulations;
    private Button repeatWorkout;
    private Button changeWorkout;
    private Button share;
    private Button resume;
    private Button pause;
    private ProgressBar spinner;

    private PowerManager.WakeLock mWakeLock;

    private long TOTAL_TIME; //5 minutes Workout
    private long EXERCISE_TIME = 20000; // 20seconds exercice time default value
    private long INTERVAL = 1000; // 1second interval
    private long REST_TIME = 10000; // 10seconds rest time default value

    private int n = 1;//variables used to change textView counter
    private int j = 1;

    private MyCountDownTimer timer;//countdown timer used to calculate time remaining for the workout

    private TextToSpeech tts;//text to speech allows speaking while counting

    private boolean ttsReady = false;//waits for text to speach to initialize

    private boolean isDone = false;

    protected String[] workouts;//array of exercises
    protected String[] videos;//array of videos

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        initWakeLock();

        initToolbar();

        initTextToSpeech();

        setSharedPreferences();

        setExercises();

        setVideos();

        counter = (TextView) findViewById(R.id.counter);
        counter.setTypeface(Typeface.createFromAsset(getApplication().getAssets(), "fonts/Roboto-Thin.ttf"));

        exercise1 = (TextView) findViewById(R.id.first_exercice);
        exercise1.setText(workouts[0]);

        exercise2 = (TextView) findViewById(R.id.second_exercice);
        exercise2.setText("Next: " + workouts[1]);

        video = (ImageView) findViewById(R.id.video_icon);
        video.setVisibility(View.GONE);

        congratulations = (TextView)findViewById(R.id.congratulations);
        congratulations.setTypeface(Typeface.createFromAsset(getApplication().getAssets(), "fonts/lobster.ttf"));
        congratulations.setVisibility(View.GONE);

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
            mWakeLock.acquire();

            new Task().execute();
        }
        if (view.getId() == R.id.counter_button_pause){
            if (mWakeLock.isHeld()){
                mWakeLock.release();
            }
            pauseCounter();
        }

        if (view.getId() == R.id.counter_button_resume){
            this.mWakeLock.acquire();
            resumeCounter();
        }

        if(view.getId() == R.id.repeat_workout){
            finish();
            startActivity(new Intent(getApplicationContext(), getClass()));
        }
        if (view.getId() == R.id.change_workout)
            onBackPressed();

        if (view.getId() == R.id.share_workout){
            String linkGoogle = "http://play.google.com/store/apps/details?id=com.benzino.fiveminworkout";
            String linkAmazon= "http://www.amazon.com/gp/mas/dl/android?p=com.benzino.fiveminworkout";
            String message = "I just finished my "+setToolbarTitle()+" and i feel great thanks to this awesome app #5minWorkout.\nDownload it from here: \n" + linkAmazon;
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, message);

            startActivity(Intent.createChooser(share, "Share your " + setToolbarTitle()));
        }
    }

    /** Called when activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        if (mWakeLock.isHeld()){
            mWakeLock.release();
        }

        if(timer!=null && !isDone){
            pauseCounter();
        }

        if (mAdView != null) {
            mAdView.pause();
        }
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    /** Called when stopping the activity */
    @Override
    protected void onStop() {
        super.onStop();
    }

    /** Called when activity is destroyed */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //release wakelock
        if(mWakeLock.isHeld())
            mWakeLock.release();

        //stop and shutdown text to speech
        if(tts != null){
            tts.stop();
            tts.shutdown();
        }

        //stop the countdown timer
        if(timer !=null){
            timer.cancel();
            timer = null;
        }
        if (mAdView != null) {
            mAdView.destroy();
        }
    }

    /**
     * Used to set the title of the toolbar
     * @return the title of the toolbar
     */
    protected abstract String setToolbarTitle();

    /**
     * Used to set the array of exercices
     */
    protected abstract void setExercises();

    /**
     * Used to set the array of videos
     */
    protected abstract void setVideos();

    /**
     * Sets the type of the workout
     * @return type of the workout
     */
    protected abstract String setType();

    public void initWakeLock(){
        //Keeping the device awake during the workout
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "MyTag");
    }

    /*
    * Creates and initialise the toolbar
    * */
    public void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.workout_toolbar);
        toolbar.setTitle(setToolbarTitle());

        toolbar.setNavigationIcon(R.mipmap.ic_arrow_left_white_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
    * Get the preferences chosen on the settings page
    * */
    public void setSharedPreferences(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        if(sp != null){
            Toast.makeText(this, "Exercice time : " + sp.getInt("exercise_time", 20) + " seconds", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Rest time : " + sp.getInt("rest_time", 10)+ " seconds", Toast.LENGTH_LONG).show();

            if(!sp.getBoolean("enable_audio", true)){
                ttsReady = true;
                tts.stop();
                tts.shutdown();
            }

            EXERCISE_TIME = sp.getInt("exercise_time", 20) * INTERVAL;
            REST_TIME = sp.getInt("rest_time", 10) * INTERVAL;
        }
    }

    /**
     * Sets listener to the video icon
     *
     */
    public void watchVideo(final String url){
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });
    }

    /**
    * Creates and initialise the text to speech feature
    * */
    @SuppressWarnings("deprecation")
    public void initTextToSpeech(){
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {

                if(status == TextToSpeech.SUCCESS){
                    Bundle bundle = new Bundle();
                    bundle.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "ANAS");

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "ANAS");

                    tts.setLanguage(Locale.US);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                        tts.speak(" ", TextToSpeech.QUEUE_FLUSH, bundle, "ANAS");
                    }else{
                        tts.speak(" ", TextToSpeech.QUEUE_FLUSH, hashMap);
                    }
                }else{
                    ttsReady = true;
                }

            }
        });

        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                ttsReady = true;
            }

            @Override
            public void onDone(String utteranceId) {
            }

            @Override
            public void onError(String utteranceId) {

            }
        });
    }

    /**
    * Used to make the device speak the message
    *
    * @param message the string that we want to say
    * @param queue the type of queue for the text to speech
    * */
    @SuppressWarnings("deprecation")
    public void speak(String message, int queue ){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(message, queue, null, null);
        }else{
            tts.speak(message, queue, null);
        }
    }

    /**
     * Called when we want text to speech to be silent
     */
    @SuppressWarnings("deprecation")
    public void silent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.playSilentUtterance(400, TextToSpeech.QUEUE_ADD, null);
        }else {
            tts.playSilence(400, TextToSpeech.QUEUE_ADD, null);
        }
    }

    /**
     * This method simply make the device say the countdown from 3 to 1
     *
     * @param message the message we want to say at the end of the countdown
     */
    public void speakCountdown(String message){
        speak("3", TextToSpeech.QUEUE_ADD);
        silent();
        speak("2", TextToSpeech.QUEUE_ADD);
        silent();
        speak("1. "+message, TextToSpeech.QUEUE_ADD);
    }

    /**
     * Model for the text to speech to talk all
     *
     * @param array the exercises we want the device to say
     */
    public void speaking(String[] array) {

        if(timer.mMillisLeft < (TOTAL_TIME) && (TOTAL_TIME - INTERVAL) <=timer.mMillisLeft){
            speak("Start " + array[0], TextToSpeech.QUEUE_ADD);
        }
        if(timer.mMillisLeft <(TOTAL_TIME - n * EXERCISE_TIME -  (n - 1) * REST_TIME + 3.3 * INTERVAL)
                && (TOTAL_TIME - n * EXERCISE_TIME - (n - 1) * REST_TIME + 2.3 * INTERVAL )<=timer.mMillisLeft && REST_TIME !=0){

            if (n == (array.length - 1)){
                speakCountdown("Your workout is done for today");
            }else{
                speakCountdown("Take a Rest. " + (REST_TIME / 1000) + " seconds");
            }
        }
        if(timer.mMillisLeft <(TOTAL_TIME - n * EXERCISE_TIME -  n * REST_TIME + 3.3 * INTERVAL)
                && (TOTAL_TIME - n * EXERCISE_TIME - n * REST_TIME + 2.3 * INTERVAL)<=timer.mMillisLeft ){
            speakCountdown("Start " + array[n]);
            n++ ;
        }
    }

    /**
     * Model for the countdown timer
     *
     * @param array the array of exercises
     * @param millisUntilFinished milliseconds until the countdown is done
     */
    public void textCounting(long millisUntilFinished, String[] array, String[] videos) {
        if (timer.mMillisLeft < TOTAL_TIME
                && (TOTAL_TIME - EXERCISE_TIME) <= timer.mMillisLeft) {
            counter.setText("" + ((millisUntilFinished - (TOTAL_TIME - EXERCISE_TIME - INTERVAL)) / INTERVAL));
            exercise1.setText(array[0]);
            exercise2.setText("Next: "+ array[1]);
            video.setVisibility(View.VISIBLE);
            watchVideo(videos[0]);
        }
        if (timer.mMillisLeft < (TOTAL_TIME - j * EXERCISE_TIME - (j - 1) * REST_TIME)
                && (TOTAL_TIME - j * EXERCISE_TIME - j * REST_TIME) <= timer.mMillisLeft) {
            counter.setText("" + ((millisUntilFinished - (TOTAL_TIME - j * EXERCISE_TIME - j * REST_TIME - INTERVAL)) / INTERVAL ));
            exercise1.setText("REST");
            video.setVisibility(View.GONE);
        }
        if (timer.mMillisLeft < (TOTAL_TIME - j * EXERCISE_TIME - j * REST_TIME)
                && (TOTAL_TIME - (j + 1) * EXERCISE_TIME - j * REST_TIME) <= timer.mMillisLeft) {

            counter.setText("" + ((millisUntilFinished - (TOTAL_TIME - (j+ 1) * EXERCISE_TIME - j * REST_TIME - INTERVAL)) / INTERVAL));
            exercise1.setText(array[j]);
            video.setVisibility(View.VISIBLE);
            watchVideo(videos[j]);

            if (j == (array.length - 2) ){
                counter.setText("" + ((millisUntilFinished - (TOTAL_TIME - (j+ 1) * EXERCISE_TIME - j * REST_TIME)) / INTERVAL));
                exercise2.setText("");
            }else {
                exercise2.setText("Next: " + array[j + 1]);
            }
        }

        if ( timer.mMillisLeft < (TOTAL_TIME - (j + 1) * EXERCISE_TIME - j * REST_TIME + INTERVAL)){
            j++;
        }
    }

    /**
     * Called when the countdown timer is done
     */
    public void done() {
        isDone = true;

        DatabaseHandler.getInstance(WorkoutActivity.this).createTest(new Test(setType()));

        j = 1;
        n = 1;

        video.setVisibility(View.GONE);
        pause.setVisibility(View.GONE);
        resume.setVisibility(View.GONE);
        counter.setVisibility(View.GONE);
        exercise1.setVisibility(View.GONE);
        exercise2.setVisibility(View.GONE);

        congratulations.setVisibility(View.VISIBLE);
        repeatWorkout.setVisibility(View.VISIBLE);
        repeatWorkout.setEnabled(true);
        changeWorkout.setVisibility(View.VISIBLE);
        changeWorkout.setEnabled(true);
        share.setVisibility(View.VISIBLE);
        share.setEnabled(true);
    }

    /**
     * Method used to start and initiate countdown time
     *
     */
    public void startCounter(){
        pause.setEnabled(true);
        pause.setVisibility(View.VISIBLE);

        TOTAL_TIME = 10 * EXERCISE_TIME + 9 * REST_TIME;

        timer = new MyCountDownTimer(TOTAL_TIME, INTERVAL) {

            /**
             * Callback fired on regular interval.
             *
             * @param millisUntilFinished The amount of time until finished.
             */

            @Override
            public void onTick(long millisUntilFinished) {
                    //Display the remaining seconds to app interface
                    //1 second = 1000 milliseconds
                    counter.setText("");

                    speaking(workouts);

                    textCounting(millisUntilFinished, workouts, videos);
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

    /**
     * Called when we want to resume the counter after a pause
     *
     */
    public void resumeCounter(){
        resume.setEnabled(false);
        resume.setVisibility((View.GONE));
        pause.setEnabled(true);
        pause.setVisibility(View.VISIBLE);

        //Resumes the timer
        timer.resume();
    }

    /**
     * Called when we want to pause the countdown timer
     */
    public void pauseCounter(){
        pause.setEnabled(false);
        pause.setVisibility(View.GONE);
        resume.setEnabled(true);
        resume.setVisibility(View.VISIBLE);

        //Pauses the timer
        timer.pause();
    }

    /**
     * Class Task used to synchronize between the text to speech
     * and the countdown timer
     */
    public class Task extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            spinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            //waits until the text to speech is ready
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

}
