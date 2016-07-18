package com.example.yi.smartschedule.lib;

import java.io.Serializable;

/**
 * Created by Yi on 7/13/16.
 */
public class EventData implements Serializable {
    private Time startTime = null;
    private Time duration = null;
    private String title = null;
    private String description = null;

    public EventData() { }

    public EventData(Time startTime, Time duration, String title, String description) {
        this.startTime = startTime;
        this.duration= duration;
        this.title = title;
        this.description = description;
    }

    public EventData setStartTime(Time startTime) {
        this.startTime = startTime;
        return this;
    }
    public Time getStartTime() {
        return this.startTime;
    }

    public Time getEndTime() {
        return this.getStartTime().addTime(this.getDuration());
    }

    public EventData setDuration(Time duration) {
        this.duration = duration;
        return this;
    }
    public Time getDuration() {
        return this.duration;
    }

    public EventData setTitle(String title) {
        this.title = title;
        return this;
    }
    public String getTitle() {
        return this.title;
    }

    public EventData setDescription(String description) {
        this.description = description;
        return this;
    }
    public String getDescription() {
        return this.description;
    }
}
