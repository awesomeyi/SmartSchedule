package com.example.yi.smartschedule.lib;

import java.io.Serializable;

/**
 * Created by Yi on 7/11/16.
 */
public class BasicTime implements Serializable, Comparable<BasicTime> {
    private int hours;
    private int minutes;

    public BasicTime(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
        this.hours += this.minutes / 60;
        this.minutes %= 60;
    }

    public BasicTime addTime(BasicTime t2) {
        return addTime(t2.hours, t2.minutes);
    }
    public BasicTime addTime(int hours, int minutes) {
        return new BasicTime(this.hours + hours, this.minutes + minutes);
    }

    public BasicTime subtractTime(BasicTime t2) {
        return subtractTime(t2.hours, t2.minutes);
    }
    public BasicTime subtractTime(int hours, int minutes) {
        return new BasicTime(this.hours - hours, this.minutes - minutes);
    }

    public BasicTime abs() {
        return new BasicTime(0, Math.abs(this.getMinutes()));
    }

    public int getMinutes() {
        return this.hours * 60 + minutes;
    }

    public double getHours() {
        return this.getMinutes() / 60.0;
    }

    public String formatStandard() {
        String apm = hours < 12 ? "a" : "p";
        int th = this.milToStandardHours(hours);
        String tm = minutes == 0 ? "" : String.format(":%1$02d", this.minutes);
        return "" + th + tm + apm;
    }

    public static int milToStandardHours(int hours) {
        int th = hours % 12;
        return th == 0 ? 12: th;
    }

    @Override
    public int compareTo(BasicTime time) {
        return ((Integer) this.getMinutes()).compareTo((Integer) time.getMinutes());
    }
}
