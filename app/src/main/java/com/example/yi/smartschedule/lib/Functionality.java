package com.example.yi.smartschedule.lib;

import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.media.AudioManager;
import android.provider.Settings;

import com.example.yi.smartschedule.lib.db.TriggerContract;
import com.example.yi.smartschedule.lib.db.TriggerDbHelper;

/**
 * Created by jackphillips on 7/15/16.
 */
public class Functionality {
    private SQLiteDatabase db;
    private Context context;

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

    public Functionality(Context context){
        TriggerDbHelper mDbHelper = new TriggerDbHelper(context);
        this.db = mDbHelper.getReadableDatabase();
        mDbHelper.onUpgrade(db, 2, 3);
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
            Util.d(type + " " + additional);
            if(additional.equalsIgnoreCase(phoneNumber)){
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
    private Location getLocation(Cursor c){
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

    private Cursor querry(String type) {

        String[] projection = {
                TriggerContract.TriggerEntry._ID,
                TriggerContract.TriggerEntry.COLUMN_NAME_TYPE,
                TriggerContract.TriggerEntry.COLUMN_NAME_ADTIONAL_INFO,
                TriggerContract.TriggerEntry.COLUMN_NAME_ACTIONS,
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