package com.example.yi.smartschedule.models;

import android.icu.util.Calendar;

import com.example.yi.smartschedule.lib.BasicTime;
import com.example.yi.smartschedule.lib.PresetIcon;

/**
 * Created by Yi on 7/21/16.
 */
public class DateManager {

    public static EventStore getEvents(Calendar date) {
        //Temporary

        EventData events[] = {
                new EventData( new BasicTime(7, 00), new BasicTime(0, 60), "Drive to work", "", new PresetIcon(PresetIcon.IC_CAR_EVENT) ),
                new EventData( new BasicTime(9, 00), new BasicTime(0, 60), "Meeting with Joe", "", new PresetIcon(PresetIcon.IC_MEETING_EVENT) ),
                new EventData( new BasicTime(10, 30), new BasicTime(1, 30), "Work on report", "Check out the group outline on google docs", new PresetIcon(PresetIcon.IC_OFFICE_EVENT)),
                new EventData( new BasicTime(13, 00), new BasicTime(0, 60), "Lunch with Donna", "", new PresetIcon(PresetIcon.IC_FOOD_EVENT)),
                new EventData( new BasicTime(14, 00), new BasicTime(0, 60), "Walk with Jane", "", new PresetIcon(PresetIcon.IC_WALKING_EVENT)),
                new EventData( new BasicTime(15, 30), new BasicTime(3, 00), "Finish Article", "Talk to Chuck about section ii and whether it works better later in the piece, and finish conclusion and citations on page 4", new PresetIcon(PresetIcon.IC_WRITING_EVENT)),
                new EventData( new BasicTime(19, 00), new BasicTime(0, 60), "Dinner with John", "", new PresetIcon(PresetIcon.IC_CULTERY_EVENT)),
                new EventData( new BasicTime(20, 30), new BasicTime(0, 60), "Watch Game of Thrones", "", new PresetIcon(PresetIcon.IC_TV_EVENT)),
                new EventData( new BasicTime(21, 30), new BasicTime(0, 60), "Evening workout", "", new PresetIcon(PresetIcon.IC_DUMBELL_EVENT)),
                new EventData( new BasicTime(22, 30), new BasicTime(0, 30), "Read", "", new PresetIcon(PresetIcon.IC_BOOKSHELF_EVENT))
        };

        return new EventStore(events);
    }

    private DateManager() { }
}
