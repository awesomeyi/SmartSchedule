package com.example.yi.smartschedule.models;

import com.example.yi.smartschedule.lib.PresetIcon;
import com.example.yi.smartschedule.lib.BasicTime;

import java.io.Serializable;

/**
 * Created by Yi on 7/13/16.
 */
public class EventData implements Serializable {
    private BasicTime startTime = null;
    private BasicTime duration = null;
    private String title = null;
    private String description = null;
    private PresetIcon icon = null;

    public EventData() { }

    public EventData(BasicTime startTime, BasicTime duration, String title, String description, PresetIcon icon) {
        this.startTime = startTime;
        this.duration= duration;
        this.title = title;
        this.description = description;
        this.icon = icon;
    }

    public EventData setStartTime(BasicTime startTime) {
        this.startTime = startTime;
        return this;
    }
    public BasicTime getStartTime() {
        return this.startTime;
    }

    public BasicTime getEndTime() {
        return this.getStartTime().addTime(this.getDuration());
    }

    public EventData setDuration(BasicTime duration) {
        this.duration = duration;
        return this;
    }
    public BasicTime getDuration() {
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

    public PresetIcon getIcon() {
        return this.icon;
    }
    public EventData setIcon(PresetIcon icon) {
        this.icon = icon;
        return this;
    }
}
