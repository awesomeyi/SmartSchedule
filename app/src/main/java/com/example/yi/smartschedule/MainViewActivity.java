package com.example.yi.smartschedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainViewActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Button realShit = (Button) findViewById(R.id.real);
        realShit.setOnClickListener(this);

        Button dunce = (Button) findViewById(R.id.dunce);
        dunce.setOnClickListener(this);
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
