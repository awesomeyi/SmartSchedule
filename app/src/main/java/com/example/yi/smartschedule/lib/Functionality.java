package com.example.yi.smartschedule.lib;

import android.content.ContentResolver;
import android.content.Context;
import android.media.AudioManager;
import android.provider.Settings;

/**
 * Created by jackphillips on 7/15/16.
 */
public class Functionality {


    public static void silencePhone(Context context){
        AudioManager audio =  (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audio.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        Util.d("Phone silenced");

    }
    public static void unSilencePhone(Context context){
        AudioManager audio =  (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
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
