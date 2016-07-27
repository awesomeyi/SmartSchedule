package com.example.yi.smartschedule.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.provider.Settings.System;

import com.example.yi.smartschedule.R;
import com.example.yi.smartschedule.lib.Functionality;
import com.example.yi.smartschedule.lib.Util;
import com.uber.sdk.android.core.auth.AccessTokenManager;
import com.uber.sdk.android.core.auth.AuthenticationError;
import com.uber.sdk.android.core.auth.LoginCallback;
import com.uber.sdk.android.core.auth.LoginManager;
import com.uber.sdk.core.auth.AccessToken;
import com.uber.sdk.core.auth.Scope;
import com.uber.sdk.rides.client.Session;
import com.uber.sdk.rides.client.SessionConfiguration;
import com.uber.sdk.rides.client.UberRidesApi;
import com.uber.sdk.rides.client.error.ApiError;
import com.uber.sdk.rides.client.error.ErrorParser;
import com.uber.sdk.rides.client.model.UserProfile;
import com.uber.sdk.rides.client.services.RidesService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FunctionalityViewActivity extends AppCompatActivity implements View.OnClickListener {
    AudioManager audio;
    private boolean wifiInabled = true;
    private ContentResolver cResolver;

    private AccessTokenManager accessTokenManager;
    private LoginManager loginManager;
    private SessionConfiguration configuration;

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

        Button rotation = (Button) findViewById(R.id.rotate);
        rotation.setOnClickListener(this);

        Button message = (Button) findViewById(R.id.message);
        message.setOnClickListener(this);

        setRingerVolume(10);

        android.content.pm.Signature[] sigs = new android.content.pm.Signature[0];
        try {
            sigs = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), PackageManager.GET_SIGNATURES).signatures;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        for (android.content.pm.Signature sig : sigs)
        {
            Util.d("Signature hashcode : " + sig.hashCode());
        }

        Button blueTooth = (Button) findViewById(R.id.bluetooth);
        blueTooth.setOnClickListener(this);
        //todo figure out uber login
        configuration = new SessionConfiguration.Builder()
                .setClientId("ystFUzxTxsCMbwi3RS7FSaYnVoyPiCOo")
                .setRedirectUri("android-app://com.example.yi.smartschedule/myapp/link/")
                .setScopes(Arrays.asList(Scope.PROFILE, Scope.RIDE_WIDGETS))
                .build();



        accessTokenManager = new AccessTokenManager(this);

        loginManager = new LoginManager(accessTokenManager,
                new SampleLoginCallback(),
                configuration,
                1113);

        loginManager.login(FunctionalityViewActivity.this);




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
            if(!System.canWrite(getApplicationContext())){
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + this.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }





        final LocationListener mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                //your code here
                Util.d("Longitude: " + location.getLongitude() + " Latitude: " + location.getLatitude());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }

        };

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, mLocationListener);
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);

        //  String locationProvider = LocationManager.GPS_PROVIDER;
        //Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        //Util.d("Longitude: " + lastKnownLocation.getLongitude() + "Latitude: " + lastKnownLocation.getLatitude());




    }
    private class SampleLoginCallback implements LoginCallback {

        @Override
        public void onLoginCancel() {
            Util.d("Canceled");
        }

        @Override
        public void onLoginError(@NonNull AuthenticationError error) {
            Util.d("ERROR " +  error.name());
        }

        @Override
        public void onLoginSuccess(@NonNull AccessToken accessToken) {
            loadProfileInfo();
        }

        @Override
        public void onAuthorizationCodeReceived(@NonNull String authorizationCode) {
            Util.d( authorizationCode);
        }
    }

    private void loadProfileInfo() {
       // loginManager.getAccessTokenManager().setAccessToken(new AccessToken("profile history", ));
        Session session = loginManager.getSession();
        RidesService service = UberRidesApi.with(session).build().createService();

        service.getUserProfile()
                .enqueue(new Callback<UserProfile>() {
                    @Override
                    public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                        if (response.isSuccessful()) {
                            Util.d(response.body().getFirstName());
                        } else {
                            ApiError error = ErrorParser.parseError(response);
                            Util.d(error.getClientErrors().get(0).getTitle());
                        }
                    }

                    @Override
                    public void onFailure(Call<UserProfile> call, Throwable t) {

                    }
                });
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
                    Functionality.setSystemBrightness(250, getApplicationContext());
                }catch (Settings.SettingNotFoundException e){
                    Util.d("" + e.toString());
                }
                break;
            case R.id.wifi:
                toggleWifi();
                break;
            case R.id.rotate:
                try{
                    toggleRotationLock();
                }catch(Exception e){
                    Util.d(e.toString());
                }
                break;
            case R.id.message:
                sendText("9143309136", "Suck My Dick");
                break;
            case R.id.bluetooth:
                toggleBluetooth();
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

    public void setMediaVolume(int volume){
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_SHOW_UI);
        Util.d("" + audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        Util.d("Phone sound set to: " +  volume);
    }

    public void setRingerVolume(int volume){
        audio.setStreamVolume(AudioManager.STREAM_RING, volume, AudioManager.FLAG_SHOW_UI);
        Util.d("" + audio.getStreamMaxVolume(AudioManager.STREAM_RING));
        Util.d("Phone ringer set to: " + volume);
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
    public void toggleRotationLock() throws Settings.SettingNotFoundException{
        boolean current = (System.getInt(cResolver, System.ACCELEROMETER_ROTATION) == 1);
        System.putInt(cResolver, System.ACCELEROMETER_ROTATION, current? 0 : 1);
        Util.d("rotion: " + !current);

    }
    public void sendText(String phoneNumber, String message){
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Util.d("Sent message");
        }

        catch (Exception e) {
            Util.d("Failed to send Text");
            e.printStackTrace();
        }
    }

    public void toggleBluetooth(){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter != null) {
            if (mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.disable();
            } else {
                mBluetoothAdapter.enable();
            }
        }
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
    @Override
    protected void onResume() {
        super.onResume();
        if (loginManager.isAuthenticated()) {
            loadProfileInfo();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("PORNHUB", String.format("onActivityResult requestCode:[%s] resultCode [%s]",
                requestCode, resultCode));


        loginManager.onActivityResult(this, requestCode, resultCode, data);
    }

}
