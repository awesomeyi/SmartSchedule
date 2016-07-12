package com.example.yi.smartschedule;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yi.smartschedule.lib.Constants;
import com.example.yi.smartschedule.lib.Time;
import com.example.yi.smartschedule.lib.Util;

public class SingleEventElement extends Fragment {

    private static final String ARG_PARAM1 = "startTime";
    private static final String ARG_PARAM2 = "duration";
    private static final String ARG_PARAM3 = "title";
    private static final String ARG_PARAM4 = "description";

    private static final int L_HOUR_HEIGHT = 70;

    private Time startTime;
    private Time duration;
    private String title;
    private String description;

    //Elements
    private TextView title_text;
    private TextView duration_text;
    private TextView description_text;
    private ImageView event_icon;
    private View event_icon_wrapper;

    public SingleEventElement() {
        // Required empty public constructor
    }

    public static SingleEventElement newInstance(Time startTime, Time duration, String title, String description) {
        SingleEventElement fragment = new SingleEventElement();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, startTime);
        args.putSerializable(ARG_PARAM2, duration);
        args.putString(ARG_PARAM3, title);
        args.putString(ARG_PARAM4, description);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            startTime = (Time) getArguments().getSerializable(ARG_PARAM1);
            duration = (Time) getArguments().getSerializable(ARG_PARAM2);
            title = getArguments().getString(ARG_PARAM3);
            description = getArguments().getString(ARG_PARAM4);
        }
    }

    private String durationText(Time start, Time end) {
        return String.format("%1$s - %2$s", start.formatStandard(), end.formatStandard());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_single_event_element, container, false);

        //Fetch all elements
        title_text = ((TextView) v.findViewById(R.id.title_text));
        duration_text = ((TextView) v.findViewById(R.id.duration_text));
        description_text = ((TextView) v.findViewById(R.id.desciption_text));
        event_icon = (ImageView) v.findViewById(R.id.event_icon);
        event_icon_wrapper = (View) v.findViewById(R.id.event_icon_wrapper);

        //Set all text boxes
        title_text.setText(title);
        Time endtime = startTime.addTime(duration);
        duration_text.setText(this.durationText(startTime, endtime));
        description_text.setText(description);

        //Shrink view to correct size
        int finalHeight = (int) Math.round(L_HOUR_HEIGHT * duration.getHours());
        v.getLayoutParams().height = Util.pixel_to_dp(this, finalHeight);
        this.resize(finalHeight);
        return v;
    }

    private void resize(int height) {

        // >= 2 hours
        if(height >= 2 * L_HOUR_HEIGHT) {
            int dTextLine = (height - 2 * L_HOUR_HEIGHT) / 20 + 1;
            description_text.setMaxLines(dTextLine);
            return;
        }

        //1 - 2 hours
        description_text.setVisibility(View.GONE);
        if(height >= 1 * L_HOUR_HEIGHT) {
            return;
        }

        //.5 - 1 hours
        duration_text.setVisibility(View.GONE);
        event_icon.getLayoutParams().width = event_icon.getLayoutParams().height;
        if(height >= .5 * L_HOUR_HEIGHT) {
            return;
        }

        // .25 - .5 hours
        event_icon_wrapper.setVisibility(View.GONE);
        title_text.setText("• • •");
        title_text.setTextSize(height - 8); //4 dp padding
        if(height >= .25 * L_HOUR_HEIGHT) {
            return;
        }

        // < .25 hours
        title_text.setVisibility(View.GONE);
    }
}