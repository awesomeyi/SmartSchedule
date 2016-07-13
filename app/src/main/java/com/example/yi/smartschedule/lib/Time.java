package com.example.yi.smartschedule.lib;

import java.io.Serializable;

/**
 * Created by Yi on 7/11/16.
 */
public class Time implements Serializable {
    private int hours;
    private int minutes;

    public Time(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
        this.hours += this.minutes / 60;
        this.minutes %= 60;
        this.hours %= 24;
    }

    public Time addTime(Time t2) {
        return addTime(t2.hours, t2.minutes);
    }
    public Time addTime(int hours, int minutes) {
        return new Time(this.hours + hours, this.minutes + minutes);
    }

    public Time subtractTime(Time t2) {
        return subtractTime(t2.hours, t2.minutes);
    }
    public Time subtractTime(int hours, int minutes) {
        return new Time(this.hours - hours, this.minutes - minutes);
    }

    public int getMinutes() {
        return this.hours * 60 + minutes;
    }

    public double getHours() {
        return this.getMinutes() / 60.0;
    }

    public String formatStandard() {
        String apm = hours < 12 ? "a" : "p";
        int th = this.hours % 12;
        th = th == 0 ? 12: th;
        String tm = minutes == 0 ? "" : String.format(":%1$02d", this.minutes);
        return "" + th + tm + apm;
    }
}
