package com.benzino.fiveminworkout;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.vanniktech.vntnumberpickerpreference.VNTNumberPickerPreference;

import java.util.List;

/**
 * Created by Anas on 4/2/16.
 */
public class SettingsActivity extends AppCompatPreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    SharedPreferences sp;
    int anas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

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
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        sp.unregisterOnSharedPreferenceChangeListener(this);
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
                    String link = "this is a link";
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
                AppCompatPreferenceActivity activity = (AppCompatPreferenceActivity) getActivity();

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