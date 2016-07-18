package com.example.yi.smartschedule.lib;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.media.AudioManager;
import android.provider.Settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jackphillips on 7/15/16.
 */
public class Functionality {
    private SQLiteDatabase db;
    private Context context;

    public Functionality(Context context){
        TriggerDbHelper mDbHelper = new TriggerDbHelper(context);
        this.db = mDbHelper.getReadableDatabase();
        this.context = context;
    }
    public void addTrigger(String type, String addtionalInfo, String actions){
        ContentValues values = new ContentValues();
        values.put(TriggerContract.TriggerEntry.COLUMN_NAME_TYPE, type);
        values.put(TriggerContract.TriggerEntry.COLUMN_NAME_ADTIONAL_INFO, addtionalInfo);
        values.put(TriggerContract.TriggerEntry.COLUMN_NAME_ACTIONS, actions);

        long newRowId = db.insert(
                TriggerContract.TriggerEntry.TABLE_NAME,
                "null",
                values);
    }
    public void phoneTrigger(String phoneNumber){
        Cursor c = querry("phoneCall");
        c.moveToFirst();
        for(int i = 0; i < c.getCount(); i++){
            String type = c.getString(
                    c.getColumnIndexOrThrow(TriggerContract.TriggerEntry.COLUMN_NAME_TYPE)
            );
            String additional = c.getString(
                    c.getColumnIndexOrThrow(TriggerContract.TriggerEntry.COLUMN_NAME_ADTIONAL_INFO)
            );
            String actions = c.getString(
                    c.getColumnIndexOrThrow(TriggerContract.TriggerEntry.COLUMN_NAME_ACTIONS)
            );
            Util.d(type + " " + additional);
            if(additional.equalsIgnoreCase(phoneNumber)){
                doActions(actions);
            }

        }
    }

    public void gpsTrigger(Location l) {
        Cursor c = querry("GPS");
        Cursor leave = querry("GPSleave");
        c.moveToFirst();
        for(int i = 0; i < c.getCount(); i++){
            String actions = c.getString(
                    c.getColumnIndexOrThrow(TriggerContract.TriggerEntry.COLUMN_NAME_ACTIONS)
            );
            Location endPoint = getLocation(c);
            if (endPoint.distanceTo(l) < 800) {
                doActions(actions);
            }
            c.moveToNext();

        }
        leave.moveToFirst();
        for(int i = 0; i < leave.getCount(); i++){
            String actions = leave.getString(
                    leave.getColumnIndexOrThrow(TriggerContract.TriggerEntry.COLUMN_NAME_ACTIONS)
            );
            Location endPoint = getLocation(leave);
            if (endPoint.distanceTo(l) > 800) {
                doActions(actions);
            }
            leave.moveToNext();

        }
    }
    public Location getLocation(Cursor c){
        String type = c.getString(
                c.getColumnIndexOrThrow(TriggerContract.TriggerEntry.COLUMN_NAME_TYPE)
        );
        String additional = c.getString(
                c.getColumnIndexOrThrow(TriggerContract.TriggerEntry.COLUMN_NAME_ADTIONAL_INFO)
        );
        Util.d(type + " " + additional);
        Location endPoint = new Location("selfMade");
        String latlong[] = additional.split(",");
        endPoint.setLongitude(Double.parseDouble(latlong[1]));
        endPoint.setLatitude(Double.parseDouble(latlong[0]));
        return endPoint;
    }
    public Cursor querry(String type) {

        String[] projection = {
                TriggerContract.TriggerEntry._ID,
                TriggerContract.TriggerEntry.COLUMN_NAME_TYPE,
                TriggerContract.TriggerEntry.COLUMN_NAME_ADTIONAL_INFO,
                TriggerContract.TriggerEntry.COLUMN_NAME_ACTIONS
        };
        Cursor c = db.query(
                TriggerContract.TriggerEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                TriggerContract.TriggerEntry.COLUMN_NAME_TYPE + "=?", // The columns for the WHERE clause
                new String[]{type},                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        /*Cursor c = db.query(
                TriggerContract.TriggerEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );*/
        return c;
    }

    public void doActions(String actions) {
        String action[] = actions.split(",");
        for(int i =0; i < action.length; i++){
            doAction(action[i]);
        }
    }

    public void doAction(String action){
        switch (action){
            case "silencePhone":
                silencePhone(context);
                break;
            case "unSilencePhone":
                unSilencePhone(context);
                break;
            case "ringerViberatePhone":
                ringerViberatePhone(context);
                break;
        }
    }

    public static void silencePhone(Context context){
        AudioManager audio =  (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audio.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        Util.d("Phone silenced");

    }
    public static void unSilencePhone(Context context){
        AudioManager audio =  (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        Util.d("Phone sound Normal");
    }
    public static void ringerViberatePhone(Context context){
        AudioManager audio =  (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        audio.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        Util.d("Phone sound Vibrate");
    }
    public static void setSystemBrightness(int brightness, Context context) throws Settings.SettingNotFoundException {
        ContentResolver cResolver = context.getContentResolver();
        //Makes Screen Brightness Manual
        Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        //Sets the brightness
        Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
        Util.d("Brightness set to: " + brightness);
        //gets current brightness:
    }
    public static int getSystemBrightness(Context context) throws Settings.SettingNotFoundException {
        ContentResolver cResolver = context.getContentResolver();
        //gets current brightness:
        int currentBrightness = Settings.System.getInt(cResolver, Settings.System.SCREEN_BRIGHTNESS);
        return currentBrightness;
    }
}
