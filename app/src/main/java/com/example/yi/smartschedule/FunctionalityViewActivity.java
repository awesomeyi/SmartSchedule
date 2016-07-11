package com.example.yi.smartschedule;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class FunctionalityViewActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_functionality_view);
        Button silence = (Button) findViewById(R.id.silent);
        silence.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.silent:
                silencePhone();
                break;

        }
    }
    public void silencePhone(){
        AudioManager audio = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        Log.d("Fuck", "Yi Budtt");
    }
}
