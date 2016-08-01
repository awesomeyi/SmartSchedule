package com.example.yi.smartschedule.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.yi.smartschedule.R;
import com.example.yi.smartschedule.lib.Functionality;
import com.example.yi.smartschedule.background.LocationService;

public class StartViewActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN}, 1);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_view);

        Button realShit = (Button) findViewById(R.id.real);
        realShit.setOnClickListener(this);

        Button dunce = (Button) findViewById(R.id.dunce);
        dunce.setOnClickListener(this);

        Functionality functionality = new Functionality(getApplicationContext());
        functionality.addTrigger(Functionality.TRIGGER_PHONECALL, "9143309136", Functionality.UNSILENCE_PHONE + "," + Functionality.SET_RINGER_VOLUME + " 13", Functionality.FILTER_TIME + " 10:15");
        startService(new Intent(this, LocationService.class));
        //functionality.addTrigger(Functionality.TRIGGER_PHONECALL, "9143309136", Functionality.SET_BRITNESS, 100);


        //functionality.gpsTrigger(new Location("J"));

    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.dunce:
                Intent intent = new Intent(this, MainViewActivity.class);
                startActivity(intent);
                break;
            case R.id.real:
                Intent intent2 = new Intent(this, FunctionalityViewActivity.class);
                startActivity(intent2);
                break;
        }
    }
}
