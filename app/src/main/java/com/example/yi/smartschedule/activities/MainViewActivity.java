package com.example.yi.smartschedule.activities;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.yi.smartschedule.R;
import com.example.yi.smartschedule.activities.TimeView.TimeViewFragment;

public class MainViewActivity extends AppCompatActivity {

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

        TimeViewFragment timeViewFragment = new TimeViewFragment();
        getSupportFragmentManager() .beginTransaction()
                                    .add(R.id.frame_container, timeViewFragment)
                                    .commit();
    }
}
