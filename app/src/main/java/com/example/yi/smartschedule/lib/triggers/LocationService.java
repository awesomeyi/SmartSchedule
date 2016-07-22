package com.example.yi.smartschedule.lib.triggers;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.yi.smartschedule.lib.Functionality;
import com.example.yi.smartschedule.lib.Util;

public class LocationService extends Service {

    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 0; //5 * 60000
    private static final float LOCATION_DISTANCE = 800;

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;
        //Functionality functionality = new Functionality(getApplicationContext());

        public LocationListener(String provider) {
            Util.d("LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Util.d("onLocationChanged: " + location);
            Functionality functionality = new Functionality(getApplicationContext());
            functionality.gpsTrigger(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Util.d("onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Util.d("onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Util.d("onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Util.d("onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Util.d("onCreate");
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Util.d("fail to request location update, ignore" + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            Util.d("network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Util.d("fail to request location update, ignore" + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            Util.d("gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        Util.d("onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Util.d("fail to remove location listners, ignore"+ ex.getMessage());
                }
            }
        }
    }

    private void initializeLocationManager() {
        Util.d( "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}