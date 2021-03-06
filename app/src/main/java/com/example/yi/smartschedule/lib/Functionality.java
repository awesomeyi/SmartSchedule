package com.example.yi.smartschedule.lib;

import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.Calendar;

import android.location.Location;
import android.media.AudioManager;
import android.provider.Settings;

import com.example.yi.smartschedule.db.TriggerContract;
import com.example.yi.smartschedule.db.TriggerDbHelper;

/**
 * Created by jackphillips on 7/15/16.
 */
public class Functionality {
    private SQLiteDatabase db;
    private Context context;

    public static final String FILTER_SEPERATOR = "--!!";
    public static final String SILENCE_PHONE = "silencePhone";
    public static final String UNSILENCE_PHONE = "unSilencePhone";
    public static final String VIBERATE_PHONE = "ringerViberatePhone";
    public static final String SET_BRITNESS = "setBrightness";
    public static final String TURN_OFF_BLUETOOTH = "turnOffBluetooth";
    public static final String TURN_ON_BLUETOOTH = "turnOnBluetooth";
    public static final String SET_MEDIA_VOLUME = "setMediaVolume";
    public static final String SET_RINGER_VOLUME = "setRingerVolume";
    public static final String SET_NOTIFICATION_VOLUME = "setNotificationVolume";


    public static final String TRIGGER_GPS_ARIVE = "GPS";
    public static final String TRIGGER_GPS_LEAVE = "GPSleave";
    public static final String TRIGGER_PHONECALL = "phoneCall";
    public static final String FILTER_TIME = "timeFILTER";

    private static final int DISTANCE = 500;

    public Functionality(Context context){
        TriggerDbHelper mDbHelper = new TriggerDbHelper(context);
        this.db = mDbHelper.getReadableDatabase();
        //mDbHelper.onUpgrade(db, 2, 3);
        this.context = context;
    }
    public void addTrigger(String type, String addtionalInfo, String actions, String filters){
        ContentValues values = new ContentValues();
        values.put(TriggerContract.TriggerEntry.COLUMN_NAME_TYPE, type);
        values.put(TriggerContract.TriggerEntry.COLUMN_NAME_ADTIONAL_INFO, addtionalInfo);
        values.put(TriggerContract.TriggerEntry.COLUMN_NAME_ACTIONS, actions);
        values.put(TriggerContract.TriggerEntry.COLUMN_NAME_FILTERS, filters);

        long newRowId = db.insert(
                TriggerContract.TriggerEntry.TABLE_NAME,
                "null",
                values);
    }
    public void addTrigger(String type, String addtionalInfo, String actions, int value){
        ContentValues values = new ContentValues();
        values.put(TriggerContract.TriggerEntry.COLUMN_NAME_TYPE, type);
        values.put(TriggerContract.TriggerEntry.COLUMN_NAME_ADTIONAL_INFO, addtionalInfo);
        values.put(TriggerContract.TriggerEntry.COLUMN_NAME_ACTIONS, actions + " " + value);


        long newRowId = db.insert(
                TriggerContract.TriggerEntry.TABLE_NAME,
                "null",
                values);
    }
    public String getActionString(String action, String value) {
        return action + " " + value;
    }

    public void phoneTrigger(String phoneNumber){
        Util.d("Phone Call");
        Cursor c = querry(TRIGGER_PHONECALL);
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
            String filters = c.getString(
                    c.getColumnIndexOrThrow(TriggerContract.TriggerEntry.COLUMN_NAME_FILTERS)
            );
            Util.d(type + " " + additional);
            if(additional.equalsIgnoreCase(phoneNumber) && checkFilters(filters)){
                doActions(actions);
            }

        }
    }

    public void gpsTrigger(Location l) {
        Cursor c = querry(TRIGGER_GPS_ARIVE);
        Cursor leave = querry(TRIGGER_GPS_LEAVE);
        c.moveToFirst();
        for(int i = 0; i < c.getCount(); i++){
            String actions = c.getString(
                    c.getColumnIndexOrThrow(TriggerContract.TriggerEntry.COLUMN_NAME_ACTIONS)
            );
            String filters = c.getString(
                    c.getColumnIndexOrThrow(TriggerContract.TriggerEntry.COLUMN_NAME_FILTERS)
            );
            Location endPoint = getCursorLocation(c);
            if (checkArivalLoction(l, endPoint) && checkFilters(filters)) {
                doActions(actions);
            }
            c.moveToNext();

        }
        leave.moveToFirst();
        for(int i = 0; i < leave.getCount(); i++){
            String actions = leave.getString(
                    leave.getColumnIndexOrThrow(TriggerContract.TriggerEntry.COLUMN_NAME_ACTIONS)
            );
            String filters = leave.getString(
                    leave.getColumnIndexOrThrow(TriggerContract.TriggerEntry.COLUMN_NAME_FILTERS)
            );
            Location endPoint = getCursorLocation(leave);
            if (checkLeaveLoction(l, endPoint) && checkFilters(filters)) {

                doActions(actions);
            }
            leave.moveToNext();

        }
    }
    private Location getCursorLocation(Cursor c){
        String type = c.getString(
                c.getColumnIndexOrThrow(TriggerContract.TriggerEntry.COLUMN_NAME_TYPE)
        );
        String additional = c.getString(
                c.getColumnIndexOrThrow(TriggerContract.TriggerEntry.COLUMN_NAME_ADTIONAL_INFO)
        );
        Util.d(type + " " + additional);
        return getLocation(additional);

    }
    private Location getLocation(String position){
        Location endPoint = new Location("selfMade");
        String latlong[] = position.split(",");
        endPoint.setLongitude(Double.parseDouble(latlong[1]));
        endPoint.setLatitude(Double.parseDouble(latlong[0]));
        return endPoint;
    }

    private Cursor querry(String type) {

        String[] projection = {
                TriggerContract.TriggerEntry._ID,
                TriggerContract.TriggerEntry.COLUMN_NAME_TYPE,
                TriggerContract.TriggerEntry.COLUMN_NAME_ADTIONAL_INFO,
                TriggerContract.TriggerEntry.COLUMN_NAME_ACTIONS,
                TriggerContract.TriggerEntry.COLUMN_NAME_FILTERS
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
    public boolean checkFilters(String filters){
        String [] filter = filters.split(FILTER_SEPERATOR);
        for (int i =0; i < filter.length; i++){
            if(!checkFilter(filter[i])){
                return false;
            }
        }
        return true;
    }
    public boolean checkFilter(String filter){
        String [] filterStuff = filter.split(" ");
        String type = filterStuff[0];
        String params = filterStuff[1];

        switch (type) {
            case FILTER_TIME:
                return checkTimeLater(params);

        }
        return false;

    }


    public boolean checkTimeLater(String time){

        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        BasicTime current = new BasicTime(hour, min);
        String [] hourmin = time.split(":");
        BasicTime newTime = new BasicTime(Integer.parseInt(hourmin[0]), Integer.parseInt(hourmin[1]));
        return (newTime.compareTo(current)) <= 0;
    }
    public boolean checkArivalLoction(Location a, Location b){
        return a.distanceTo(b) < DISTANCE;
    }
    public boolean checkLeaveLoction(Location a, Location b){
        return a.distanceTo(b) > DISTANCE;
    }
    public boolean checkPhoneNumber(String number1, String number2){
        return number1.equalsIgnoreCase(number2);
    }

    private void doActions(String actions) {
        String action[] = actions.split(",");
        for(int i =0; i < action.length; i++){
            if((action[i].startsWith("set"))) {
                String [] actval = action[i].split(" ");
                doAction(actval[0], Integer.parseInt(actval[1]));
            }
            else {
                doAction(action[i], 0);
            }
        }
    }

    private void doAction(String action, int value){
        switch (action){
            case SILENCE_PHONE:
                silencePhone(context);
                break;
            case UNSILENCE_PHONE:
                unSilencePhone(context);
                break;
            case VIBERATE_PHONE:
                ringerViberatePhone(context);
                break;
            case TURN_OFF_BLUETOOTH:
                turnOffBletooth();
                break;
            case TURN_ON_BLUETOOTH:
                turnOnBletooth();
                break;
            case SET_RINGER_VOLUME:
                setRingerVolume(value, context);
                break;
            case SET_NOTIFICATION_VOLUME:
                setNotificationVolume(value, context);
                break;
            case SET_MEDIA_VOLUME:
                setMediaVolume(value, context);
                break;
            case SET_BRITNESS:
                try {
                    setSystemBrightness(value, context);
                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                }
                break;
        }
    }



    public static void setMediaVolume(int volume, Context context){
        AudioManager audio =  (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_SHOW_UI);
        Util.d("" + audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        Util.d("Phone sound set to: " +  volume);
    }
    public static void setNotificationVolume(int volume, Context context){
        AudioManager audio =  (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audio.setStreamVolume(AudioManager.STREAM_NOTIFICATION, volume, AudioManager.FLAG_SHOW_UI);
        Util.d("" + audio.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION));
        Util.d("Phone sound set to: " +  volume);
    }

    public static void setRingerVolume(int volume, Context context){
        AudioManager audio =  (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audio.setStreamVolume(AudioManager.STREAM_RING, volume, AudioManager.FLAG_SHOW_UI);
        Util.d("" + audio.getStreamMaxVolume(AudioManager.STREAM_RING));
        Util.d("Phone ringer set to: " + volume);
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

    public void turnOffBletooth() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter != null) {
            if (mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.disable();
            }
        }
    }
    public void turnOnBletooth(){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter != null) {
            if (!mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.enable();
            }
        }
    }
}