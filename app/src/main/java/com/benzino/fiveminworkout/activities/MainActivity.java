package com.benzino.fiveminworkout.activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.benzino.fiveminworkout.MyApplication;
import com.benzino.fiveminworkout.R;
import com.benzino.fiveminworkout.fragments.ProgressFragment;
import com.benzino.fiveminworkout.fragments.WorkoutFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anas on 23/1/16.
 */
public class MainActivity extends AppCompatActivity {
    private Tracker mTracker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Analytics
        // Obtain the shared Tracker instance.
        MyApplication application = (MyApplication) getApplication();
        mTracker = application.getDefaultTracker();

        //Creating the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        TextView title = (TextView) toolbar.findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Typeface font = Typeface.createFromAsset(getApplication().getAssets(), "fonts/lobster.ttf");

        title.setTypeface(font);

        //Creating the ViewPager and passing in the fragment manager
        //titles of the toolbar, and number of tabs

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), MainActivity.this);
        adapter.addFragment(new WorkoutFragment(), "Workout");
        adapter.addFragment(new ProgressFragment(), "Progress");


        //Assign the ViewPager View and setting the adapter
        final ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String name = (String) pager.getAdapter().getPageTitle(position);

                mTracker.setScreenName("Image~" + name);
                mTracker.send(new HitBuilders.ScreenViewBuilder().build());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        //Setting the ViewPager for the TabLayout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(pager);

    }

    static class PagerAdapter extends FragmentStatePagerAdapter {

        private final Context context;
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public PagerAdapter(FragmentManager fragmentManager, Context context) {
            super(fragmentManager);
            this.context = context;
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {

            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
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
        String linkGoogle = "http://play.google.com/store/apps/details?id=com.benzino.fiveminworkout";
        String linkAmazon= "http://www.amazon.com/gp/mas/dl/android?p=com.benzino.fiveminworkout";

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            return true;
        }
        else if(id == R.id.action_share) {
            String link = "http://play.google.com/store/apps/details?id=com.benzino.fiveminworkout";
            String message = "Check out this awesome app i just installed, it helps me lose fat and get in shape.\nDownload it from here: \n" + linkGoogle;
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
                        Uri.parse(linkGoogle)));
            }
        }


        return super.onOptionsItemSelected(item);
    }
}
