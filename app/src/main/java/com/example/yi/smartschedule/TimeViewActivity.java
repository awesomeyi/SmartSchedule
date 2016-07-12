package com.example.yi.smartschedule;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yi.smartschedule.lib.Time;

public class TimeViewActivity extends AppCompatActivity {
    private LinearLayout event_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_view);

        Time startTime = new Time(1, 00);
        String title = "Meet Ryan";
        String description = "Meet Ryan near the corner of 5th Ave.\nHe's going to be selling you drugs so bring money.";

        int minutes[] = {20, 30, 40, 60, 90, 120, 180};

        event_list = (LinearLayout) findViewById(R.id.event_list);
        for(int t : minutes) {

            //Create fragment
            Time duration = new Time(0, t);
            Fragment event = SingleEventElement.newInstance(startTime, duration, title, description);
            FragmentTransaction fragTrans =  getFragmentManager().beginTransaction().add(event_list.getId(), event, "fff" + t);
            fragTrans.commit();
            getFragmentManager().executePendingTransactions();
        }

    }
}
