package com.example.yi.smartschedule;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yi.smartschedule.lib.EventAdapter;
import com.example.yi.smartschedule.lib.EventData;
import com.example.yi.smartschedule.lib.Time;

public class TimeViewActivity extends AppCompatActivity {
    private LinearLayout event_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_view);

        RecyclerView main_event_list = (RecyclerView) findViewById(R.id.main_event_list);

        Time startTime = new Time(1, 00);
        String title = "Meet Ryan";
        String description = "Meet Ryan near the corner of 5th Ave.\nHe's going to be selling you drugs so bring money.";

        int minutes[] = {10, 20, 30, 40, 60, 90, 120, 180};
        EventData events[] = new EventData[minutes.length];

        for(int i = 0; i < minutes.length; ++i) {
            events[i] = new EventData(startTime,  new Time(0, minutes[i]), title, description);
        }
        EventAdapter adapter = new EventAdapter(events);
        main_event_list.setAdapter(adapter);
        main_event_list.setLayoutManager(new LinearLayoutManager(this));

    }
}
