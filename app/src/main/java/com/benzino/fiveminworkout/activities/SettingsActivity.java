package com.benzino.fiveminworkout.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.benzino.fiveminworkout.R;
import com.benzino.fiveminworkout.helpers.MyPreferenceActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by Anas on 4/2/16.
 */
public class SettingsActivity extends MyPreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    SharedPreferences sp;
    int anas;
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //ADS
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new MyPreferenceFragment())
                .commit();

        sp = PreferenceManager.getDefaultSharedPreferences(this);

        String strUserName = sp.getString("username", "NA");
        boolean bAppUpdates = sp.getBoolean("applicationUpdates", false);
        String downloadType = sp.getString("downloadType", "1");
        int anass = sp.getInt("preference_value", 0);


        //Toast.makeText(getApplicationContext(), strUserName, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return MyPreferenceFragment.class.getName().equals(fragmentName);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        anas = sharedPreferences.getInt("exercise_time", 12);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        sp.registerOnSharedPreferenceChangeListener(this);
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        sp.unregisterOnSharedPreferenceChangeListener(this);

        if (mAdView != null) {
            mAdView.pause();
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


    public static class MyPreferenceFragment extends PreferenceFragment {
        private Preference feedback;
        private Preference share;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            feedback = this.findPreference("feedback");
            feedback.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent Email = new Intent(Intent.ACTION_SEND);
                    Email.setType("text/email");
                    Email.putExtra(Intent.EXTRA_EMAIL, new String[] { "benzinoanas@gmail.com" });
                    Email.putExtra(Intent.EXTRA_SUBJECT, "Feedback 5min Workout app");
                    Email.putExtra(Intent.EXTRA_TEXT, "Dear Developer," + "\n");
                    startActivity(Intent.createChooser(Email, "Send Feedback:"));

                    return true;
                }
            });

            share = this.findPreference("share");
            share.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    String link = "http://play.google.com/store/apps/details?id=com.benzino.fiveminworkout";
                    String message = "Check out this awesome app i just installed, it helps me lose fat and improve my health.\nDownload it from here: \n" + link;
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.putExtra(Intent.EXTRA_TEXT, message);
                    startActivity(Intent.createChooser(share, "Share 5 minute Workout"));

                    return true;
                }
            });

        }



        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View layout = inflater.inflate(R.layout.activity_settings, container, false);
            if (layout != null) {
                MyPreferenceActivity activity = (MyPreferenceActivity) getActivity();

                /**
                 * Settings Toolbar
                 * */
                Toolbar toolbar = (Toolbar) layout.findViewById(R.id.toolbar_settings);
                activity.setSupportActionBar(toolbar);

                ActionBar bar = activity.getSupportActionBar();
                bar.setHomeButtonEnabled(true);
                bar.setDisplayHomeAsUpEnabled(true);
                bar.setDisplayShowTitleEnabled(true);
                bar.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        getActivity().finish();
                    }
                });
            }
            return layout;
        }
    }

}