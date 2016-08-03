package com.example.yi.smartschedule.lib;

import java.io.Serializable;
import java.util.Locale;

/**
 * Created by Yi on 7/11/16.
 * Use: anything that can be represented in an x quantity of minutes
 */
public class BasicTime implements Serializable, Comparable<BasicTime> {
    private int hours;
    private int minutes;

    public static BasicTime create(int hours, int minutes) {
        return new BasicTime(hours, minutes);
    }

    public BasicTime(int hours, int minutes) {
        this.hours = hours;
        this.hours += minutes / 60;
        this.minutes = minutes % 60;
    }

    public int getHours() { return this.hours; }
    public int getMinutes() {return this.minutes; }

    public BasicTime clone() {
        return new BasicTime(this.getHours(), this.getMinutes());
    }

    //Basic time math
    public BasicTime addTime(BasicTime t2) {
        return addTime(t2.getHours(), t2.getMinutes());
    }
    public BasicTime addTime(int hours, int minutes) {
        return new BasicTime(this.getHours() + hours, this.getMinutes() + minutes);
    }

    public BasicTime subtractTime(BasicTime t2) {
        return subtractTime(t2.getHours(), t2.getMinutes());
    }
    public BasicTime subtractTime(int hours, int minutes) {
        return new BasicTime(this.getHours() - hours, this.getMinutes() - minutes);
    }

    public BasicTime abs() {
        return new BasicTime(0, Math.abs(this.getMinutes()));
    }
    public double getApproxHours() {
        return this.getTotalMinutes() / 60.0;
    }
    public int getTotalMinutes() {
        return this.hours * 60 + minutes;
    }

    //Convert an arbitrary duration to something on a 24 hour scales
    public BasicTime normalize() {
        int hours = this.getHours();
        int minutes = this.minutes;
        if(minutes < 0) {
            --hours;
            minutes += 60;
        }
        hours %= 24;
        if(hours < 0)
            hours += 24;
        return new BasicTime(hours, minutes);
    }

    @Override
    public int compareTo(BasicTime time) {
        return ((Integer) this.getTotalMinutes()).compareTo((Integer) time.getTotalMinutes());
    }

    public static class BTFormat {
        public String format = "";
        public String hour = "";
        public String minute = "";
        public String meridium = "";

        @Override
        public String toString() {
            return String.format(Locale.US, format, hour, minute, meridium);
        }
    }

    //Format functions

    //Standard hour:minute:AM/PM - Everything positive!!
    public BTFormat formatStandard() {
        BasicTime time = this.normalize();
        //Util.d("" + this.formatDebug() + " " + time.formatDebug());
        BTFormat bf = new BTFormat();
        bf.hour = "" + milToStandardHours(time.getHours());
        bf.minute = String.format(Locale.US, "%1$02d", time.getMinutes() );
        bf.meridium = time.getHours() < 12 ? "am" : "pm";
        bf.format = "%s:%s %s";
        return bf;
    }

    //Compressed format
    public BTFormat formatCompressed() {
        BasicTime time = this.normalize();
        BTFormat bf = new BTFormat();
        bf.hour = "" + Math.abs(milToStandardHours(time.getHours()));
        bf.minute = time.getMinutes() == 0 ? "" : String.format(Locale.US, ":%1$02d", time.getMinutes());
        bf.meridium = time.getHours() < 12 ? "a" : "p";
        bf.format = "%s%s%s";
        return bf;
    }

    //Debug format
    public BTFormat formatDebug() {
        BTFormat bf = new BTFormat();
        bf.hour = "" + this.getHours();
        bf.minute = "" + this.getMinutes();
        bf.format = "%sh %sm";
        return bf;
    }

    //hours into the range [1, 12]
    public static int milToStandardHours(int hours) {
        int th = hours % 12;
        return th == 0 ? 12: th;
    }
}
