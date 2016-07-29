package com.example.yi.smartschedule.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.yi.smartschedule.R;
import com.example.yi.smartschedule.activities.DetailView.DetailViewFragment;
import com.example.yi.smartschedule.activities.TimeView.TimeViewFragment;
import com.example.yi.smartschedule.models.EventData;

public class MainViewActivity extends AppCompatActivity
        implements TimeViewFragment.TimeViewListener {

    public static int HALF_HOUR_HEIGHT() {
        return 30;
    }
    public static int FULL_HOUR_HEIGHT() {
        return HALF_HOUR_HEIGHT() * 2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);

        Toolbar top_toggle_bar = (Toolbar) findViewById(R.id.top_toggle_bar);
        setSupportActionBar(top_toggle_bar);
        if (savedInstanceState == null) {

            TimeViewFragment timeViewFragment = new TimeViewFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_container, timeViewFragment)

                    .commit();

        }
    }

    @Override
    public void onEventClick(EventData event) {
        DetailViewFragment detailEventFragment = new DetailViewFragment();
        getSupportFragmentManager() .beginTransaction()
                                    .add(R.id.frame_container, detailEventFragment)
                                    .commit();

    }
}
