package com.example.yi.smartschedule;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.yi.smartschedule.lib.LocationService;
import com.example.yi.smartschedule.lib.MyPhoneStateListener;

public class MainViewActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},1);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Button realShit = (Button) findViewById(R.id.real);
        realShit.setOnClickListener(this);

        Button dunce = (Button) findViewById(R.id.dunce);
        dunce.setOnClickListener(this);

        startService(new Intent(this, LocationService.class));

    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.dunce:
                Intent intent = new Intent(this, TimeViewActivity.class);
                startActivity(intent);
                break;
            case R.id.real:
                Intent intent2 = new Intent(this, FunctionalityViewActivity.class);
                startActivity(intent2);
                break;
        }
    }
}
