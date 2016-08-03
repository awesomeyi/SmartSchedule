package com.example.yi.smartschedule.models.TimeView;

import com.example.yi.smartschedule.activities.MainViewActivity;
import com.example.yi.smartschedule.lib.BasicTime;
import com.example.yi.smartschedule.models.EventData;

/**
 * Created by Yi on 7/20/16.
 */
public class EventBlock {
    private EventData event = null;
    private boolean visible = false;
    private double height = 0;

    public static EventBlock create() {
        return new EventBlock();
    }

    //Height
    public double getHeight() {
        return this.height;
    }

    //Set height in DP
    public EventBlock setHeight(double height) {
        this.height = height;
        return this;
    }
    public EventBlock setHeight(int height) {
        this.height = height;
        return this;
    }
    //Set height according to event height
    public EventBlock setHeight(EventData event) {
        return this.setHeight(event.getDuration());
    }
    //Set height according to duration
    public EventBlock setHeight(BasicTime duration) {
        this.height = (int) Math.round(MainViewActivity.FULL_HOUR_HEIGHT() * duration.getApproxHours());
        return this;
    }

    //Visibility
    public boolean getVisibility() { return this.visible; }
    public EventBlock setVisibility(boolean visible) {
        this.visible = visible;
        return this;
    }

    //Event
    public EventData getEvent() { return this.event; }
    public EventBlock setEvent(EventData event) {
        this.event = event;
        return this;
    }

}