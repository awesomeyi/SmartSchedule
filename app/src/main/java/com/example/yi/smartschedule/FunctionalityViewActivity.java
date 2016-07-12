package com.example.yi.smartschedule;

import android.content.ContentResolver;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.provider.Settings.System;

import com.example.yi.smartschedule.lib.Util;

import java.util.Set;

public class FunctionalityViewActivity extends AppCompatActivity implements View.OnClickListener {
    AudioManager audio;
    private ContentResolver cResolver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_functionality_view);

        audio =  (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        cResolver = getContentResolver();

        Button silence = (Button) findViewById(R.id.silent);
        silence.setOnClickListener(this);

        Button brightness = (Button) findViewById(R.id.brightness);
        brightness.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.silent:
                if(audio.getRingerMode() == AudioManager.RINGER_MODE_SILENT){
                    unSilencePhone();
                }else {
                    silencePhone();
                }
                break;
            case R.id.brightness:
                try {
                    setSystemBrightness(70);
                }catch (Settings.SettingNotFoundException e){
                    Util.d("" + e.toString());
                }
                break;

        }
    }
    public void silencePhone(){
        audio.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        Util.d("Phone silenced");
    }
    public void unSilencePhone(){
        audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        Util.d("Phone sound Normal");
    }
    public void ringerViberatePhone(){
        audio.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        Util.d("Phone sound Vibrate");
    }
    public void setSystemBrightness(int brightness) throws Settings.SettingNotFoundException {
        //gets current brightness:
        int currentBrightness = System.getInt(cResolver, System.SCREEN_BRIGHTNESS);
        Util.d("current Brightness: " + currentBrightness);
        //Makes Screen Brightness Manual
        System.putInt(cResolver, System.SCREEN_BRIGHTNESS_MODE, System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        //Sets the brightness
        System.putInt(cResolver, System.SCREEN_BRIGHTNESS, brightness);
        Util.d("Brightness set to: " + brightness);
        //gets current brightness:
    }
    //this is a secure setting so it is read only
    public void turnOnAirplaneMode(){
        boolean isEnabled = Settings.Global.getInt(getContentResolver(),Settings.Global.AIRPLANE_MODE_ON, 0) == 1;
        // toggle airplane mode
        Settings.Global.putInt(getContentResolver(),Settings.Global.AIRPLANE_MODE_ON, isEnabled ? 0 : 1);
    }
}
