package com.example.yi.smartschedule;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.yi.smartschedule.lib.Time;

public class TimeViewActivity extends AppCompatActivity {
    private LinearLayout event_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_view);

        Time startTime = new Time(1, 00);
        Time duration = new Time(1, 00);
        String title = "Meet Ryan";
        String description = "Bang for 1 hour";

        event_list = (LinearLayout) findViewById(R.id.event_list);
        Fragment event1 = SingleEventElement.newInstance(startTime, duration, title, description);
        FragmentTransaction fragTrans =  getFragmentManager().beginTransaction().add(event_list.getId(), event1, "fff");
        fragTrans.commit();
    }
}
