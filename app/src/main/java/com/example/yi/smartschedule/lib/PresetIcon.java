package com.example.yi.smartschedule.lib;

import android.content.Context;

/**
 * Created by Yi on 7/20/16.
 */
public class PresetIcon {

    //Registry
    public static final String IC_BOOKSHELF_EVENT = "bookshelf_event";
    public static final String IC_CAR_EVENT = "car_event";
    public static final String IC_CULTERY_EVENT = "cutlery_event";
    public static final String IC_DUMBELL_EVENT = "dumbbell_event";
    public static final String IC_FOOD_EVENT = "food_event";
    public static final String IC_MEETING_EVENT = "meeting_event";
    public static final String IC_OFFICE_EVENT = "office_event";
    public static final String IC_TV_EVENT = "tv_event";
    public static final String IC_WALKING_EVENT = "walking_event";
    public static final String IC_WRITING_EVENT = "writing_event";


    private String code;
    public PresetIcon(String code) {
        this.code = code;
    }

    public int getIconId(Context ctx) {
        String rname = this.code + "_icon";
        return ctx.getResources().getIdentifier(rname, "drawable", ctx.getPackageName());
    }

}
