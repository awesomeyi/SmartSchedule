package com.example.yi.smartschedule;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.provider.Settings.System;

import com.example.yi.smartschedule.lib.Util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class FunctionalityViewActivity extends AppCompatActivity implements View.OnClickListener {
    AudioManager audio;
    private boolean wifiInabled = true;
    private ContentResolver cResolver;
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_functionality_view);

        audio =  (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        cResolver = getContentResolver();

        Button silence = (Button) findViewById(R.id.silent);
        silence.setOnClickListener(this);

        Button wifi = (Button) findViewById(R.id.wifi);
        wifi.setOnClickListener(this);

        Button brightness = (Button) findViewById(R.id.brightness);
        brightness.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(!System.canWrite(getApplicationContext())){
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + this.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }


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
            case R.id.wifi:
                toggleWifi();
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

    public void toggleWifi(){
        wifiInabled = !wifiInabled;
        WifiManager wifiManager = (WifiManager)this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(wifiInabled);
        Util.d("wifi set to: " + wifiInabled);

    }
    //this is a secure setting so it is read only
    public void toggleOnAirplaneMode() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        //need root for this
        try{
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            Method setMobileDataEnabledMethod = telephonyManager.getClass().getDeclaredMethod("setDataEnabled", boolean.class);

            if(null!= setMobileDataEnabledMethod){
                setMobileDataEnabledMethod.invoke(telephonyManager, false);
                Util.d("Almost done");
            }
        }catch (Exception e){
            Util.d(e.toString());
        }

        Util.d("Almost airplane mode ");

    }
}
