package com.example.yi.smartschedule;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.example.yi.smartschedule.lib.EventAdapter;
import com.example.yi.smartschedule.lib.EventData;
import com.example.yi.smartschedule.lib.EventStore;
import com.example.yi.smartschedule.lib.PresetIcon;
import com.example.yi.smartschedule.lib.Time;
import com.example.yi.smartschedule.lib.TimeLineAdapter;

import aligningrecyclerview.AligningRecyclerView;
import aligningrecyclerview.AlignmentManager;

public class TimeViewActivity extends AppCompatActivity {

    public static int HALF_HOUR_HEIGHT() {
        return 35;
    }
    public static int FULL_HOUR_HEIGHT() {
        return HALF_HOUR_HEIGHT() * 2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_view);

        AligningRecyclerView time_line_list = (AligningRecyclerView) findViewById(R.id.time_line_list);
        AligningRecyclerView main_event_list = (AligningRecyclerView) findViewById(R.id.main_event_list);

        EventData events[] = {
            new EventData( new Time(7, 00), new Time(0, 60), "Drive to work", "", new PresetIcon(PresetIcon.IC_CAR_EVENT) ),
            new EventData( new Time(9, 00), new Time(0, 60), "Meeting with Joe", "", new PresetIcon(PresetIcon.IC_MEETING_EVENT) ),
            new EventData( new Time(10, 30), new Time(1, 30), "Work on report", "Check out the group outline on google docs", new PresetIcon(PresetIcon.IC_OFFICE_EVENT)),
            new EventData( new Time(13, 00), new Time(0, 60), "Lunch with Donna", "", new PresetIcon(PresetIcon.IC_FOOD_EVENT)),
            new EventData( new Time(14, 00), new Time(0, 60), "Walk with Jane", "", new PresetIcon(PresetIcon.IC_WALKING_EVENT)),
            new EventData( new Time(15, 30), new Time(3, 00), "Finish Article", "Talk to Chuck about section ii and whether it works better later in the piece, and finish conclusion and citations on page 4", new PresetIcon(PresetIcon.IC_WRITING_EVENT)),
            new EventData( new Time(19, 00), new Time(0, 60), "Dinner with John", "", new PresetIcon(PresetIcon.IC_CULTERY_EVENT)),
            new EventData( new Time(20, 30), new Time(0, 60), "Watch Game of Thrones", "", new PresetIcon(PresetIcon.IC_TV_EVENT)),
            new EventData( new Time(21, 30), new Time(0, 60), "Evening workout", "", new PresetIcon(PresetIcon.IC_DUMBELL_EVENT)),
            new EventData( new Time(22, 30), new Time(0, 30), "Read", "", new PresetIcon(PresetIcon.IC_BOOKSHELF_EVENT))
        };

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
    }
}
