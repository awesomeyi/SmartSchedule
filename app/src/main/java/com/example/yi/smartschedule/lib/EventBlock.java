package com.example.yi.smartschedule.lib;

import com.example.yi.smartschedule.TimeViewActivity;

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
    public EventBlock setHeight(Time duration) {
        this.height = (int) Math.round(TimeViewActivity.FULL_HOUR_HEIGHT() * duration.getHours());
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