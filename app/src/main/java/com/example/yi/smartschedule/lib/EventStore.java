package com.example.yi.smartschedule.lib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Yi on 7/15/16.
 */
public class EventStore {

    private static class c_EventData_start implements Comparator<EventData> {

        @Override
        public int compare(EventData e1, EventData e2) {
            int comp =  e1.getStartTime().compareTo(e2.getStartTime());
            if(comp != 0)
                return comp;
            return e1.getEndTime().compareTo(e2.getEndTime());
        }
    }

    private static class c_EventData_start_only implements Comparator<EventData> {

        @Override
        public int compare(EventData e1, EventData e2) {
            return e1.getStartTime().compareTo(e2.getStartTime());
        }
    }

    private static class c_EventData_end implements Comparator<EventData> {

        @Override
        public int compare(EventData e1, EventData e2) {
            int comp =  e1.getEndTime().compareTo(e2.getEndTime());
            if(comp != 0)
                return comp;
            return e1.getStartTime().compareTo(e2.getStartTime());
        }
    }

    private static class c_EventData_end_only implements Comparator<EventData> {

        @Override
        public int compare(EventData e1, EventData e2) {
            return e1.getEndTime().compareTo(e2.getEndTime());
        }
    }

    private ArrayList<EventData> byStart = new ArrayList<>();
    private ArrayList<EventData> byEnd = new ArrayList<>();

    public EventStore() { }
    public EventStore(EventData events []) {
        this.add(events);
    }

    public EventStore add(EventData events []) {
        byStart.addAll(new ArrayList<>(Arrays.asList(events)));
        Collections.sort(byStart, new c_EventData_start());
        byEnd.addAll(new ArrayList<>(Arrays.asList(events)));
        Collections.sort(byEnd, new c_EventData_end());

        for(EventData e : byStart)
            Util.d("ByStart: " + e.getStartTime().formatStandard() + "-" + e.getEndTime().formatStandard() + ",");
//        for(EventData e : byEnd)
//            Util.d("ByEnd: " + e.getStartTime().formatStandard() + "-" + e.getEndTime().formatStandard() + ",");
        return this;
    }

    public EventData first() {
        return byStart.get(0);
    }

    public EventData last() {
        return byEnd.get(byEnd.size() - 1);
    }

    //Sequential at: position by start
    public EventData at(int position) {
        if(position >= byStart.size() || position < 0)
            return null;
        return byStart.get(position);
    }

    //Event at a certain time
    public EventData at(Time time) {
        int idx = Collections.binarySearch(byStart, new EventData(time, null, null, null), new c_EventData_start_only());
        return this.at(idx);
    }

    //Which position is this event
    public int where(EventData event) {
        int idx = Collections.binarySearch(byStart, new EventData(event.getStartTime(), null, null, null), new c_EventData_start_only());
        EventData here = this.at(idx);
        if(here == null)
            return -1;
        return idx;
    }

    //What event is happening at time
    public EventData happening(Time time) {
        int idx = Collections.binarySearch(byStart, new EventData(time, null, null, null), new c_EventData_start_only());
        Util.d("" + idx);
        EventData here = this.at(idx);
        if(here != null)
            return here;
        //Not found
        for(int i = 0; i < -idx; ++i) {
            EventData cur = byStart.get(i);
            int mindiff = time.subtractTime(cur.getStartTime()).getMinutes();
            if(mindiff >= 0 && mindiff < cur.getDuration().getMinutes()) {
                return cur;
            }
        }
        return null;
    }

    public boolean startEndInInterval(Time t1, Time t2) {
        //Start time falls within range?
        int idx = Collections.binarySearch(byStart, new EventData(t1, null, null, null), new c_EventData_start_only());
        if(idx >= 0)
            return true;
        EventData next = this.at(-idx - 1);
        if(next != null && next.getStartTime().getMinutes() < t2.getMinutes()) {
            return true;
        }

        //Endtimes fall within range?
        idx = Collections.binarySearch(byEnd, new EventData(new Time(0, 0), t1, null, null), new c_EventData_end_only());
        if(idx >= 0)
            return true;
        next = this.at(-idx - 1);
        if(next != null && next.getEndTime().getMinutes() < t2.getMinutes())
            return true;

        return false;
    }

    //Event between a certain time
    public boolean exists(Time t1, Time t2) {
        //Does the event enclose t1 and t2? and are the start and end times within the interval?
        if(this.startEndInInterval(t1, t2) && this.happening(t1) != null)
            return true;
        return false;
    }

    public int count() {
        return byStart.size();
    }
}
