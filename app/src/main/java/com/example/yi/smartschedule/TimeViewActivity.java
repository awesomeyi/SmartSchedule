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
import com.example.yi.smartschedule.lib.EventStore;
import com.example.yi.smartschedule.lib.Time;
import com.example.yi.smartschedule.lib.TimeLineAdapter;
import com.example.yi.smartschedule.lib.Util;

import aligningrecyclerview.AligningRecyclerView;
import aligningrecyclerview.AlignmentManager;

public class TimeViewActivity extends AppCompatActivity {
    private LinearLayout event_list;
    public static final int L_BLOCK_HEIGHT = 70;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_view);

        AligningRecyclerView time_line_list = (AligningRecyclerView) findViewById(R.id.time_line_list);
        AligningRecyclerView main_event_list = (AligningRecyclerView) findViewById(R.id.main_event_list);

        String title = "Meet Ryan";
        String description = "Meet Ryan near the corner of 5th Ave.\nHe's going to be selling you drugs so bring money.";

        Time startTimes[] = { new Time(1, 00), new Time(2, 00), new Time(4, 00), new Time(5, 30), new Time(6, 00), new Time(10, 00)};
        int minutes[] = {30, 40, 60, 20, 80, 30};
        EventData events[] = new EventData[minutes.length];

        for(int i = 0; i < minutes.length; ++i) {
            events[i] = new EventData(startTimes[i],  new Time(0, minutes[i]), title, description);
        }

        EventStore eventStore = new EventStore(events);
        TimeLineAdapter timeLineAdapter = new TimeLineAdapter(eventStore);
        EventAdapter eventAdapter = new EventAdapter(eventStore);

        final @AligningRecyclerView.AlignOrientation int orientation;
        orientation = AligningRecyclerView.ALIGN_ORIENTATION_VERTICAL;

        time_line_list.setAdapter(timeLineAdapter);
        time_line_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        main_event_list.setAdapter(eventAdapter);
        main_event_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        AlignmentManager.join(orientation, time_line_list, main_event_list);

        Util.d("" + eventStore.startEndInInterval(new Time(7, 01), new Time(7, 59)));
    }
}
