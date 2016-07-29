package com.example.yi.smartschedule.activities.DetailView;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.example.yi.smartschedule.R;
import com.example.yi.smartschedule.lib.BasicTime;
import com.example.yi.smartschedule.lib.EndlessScrollListener;
import com.example.yi.smartschedule.lib.Util;

/**
 * A simple {@link Fragment} subclass.
 */

public class TimePickerFragment extends Fragment {

    private static String P_START = "startTime";
    private static String P_LIMIT = "limit";
    private static String P_REVERSE = "reverse";

    private BasicTime startTime, limit;
    private boolean reverse = false;

    private View myView;
    private RecyclerView time_picker_list;
    private View picker_button, picker_layout;
    private TextView hour_text, minute_text;

    private TimePickerAdapter timePickerAdapter;
    private LinearLayoutManager timePickerLayout;


    public static TimePickerFragment newInstance(BasicTime startTime, BasicTime limit, boolean reverse) {
        TimePickerFragment myFragment = new TimePickerFragment();

        Bundle args = new Bundle();
        args.putSerializable(P_START, startTime);
        args.putSerializable(P_LIMIT, limit);
        args.putBoolean(P_REVERSE, reverse);

        myFragment.setArguments(args);
        return myFragment;
    }

    public TimePickerFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle b = getArguments();
            startTime = (BasicTime) b.getSerializable(P_START);
            limit = (BasicTime) b.getSerializable(P_LIMIT);
            reverse = b.getBoolean(P_REVERSE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.fragment_time_picker, container, false);

        //Init elements
        picker_layout = v.findViewById(R.id.picker_layout);
        picker_button = v.findViewById(R.id.picker_button);
        time_picker_list = (RecyclerView) v.findViewById(R.id.time_picker_list);

        hour_text = (TextView) v.findViewById(R.id.hour_text);
        minute_text = (TextView) v.findViewById(R.id.minute_text);

        this.setTimeText();

        picker_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picker_layout.setVisibility(View.VISIBLE);
                picker_button.setVisibility(View.INVISIBLE);
                scrollToTime();
            }
        });

        timePickerLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, reverse);
        timePickerAdapter = new TimePickerAdapter(limit, reverse, new TimePickerAdapter.TimeSelectListener() {
            @Override
            public void onTimeClick(BasicTime time) {
                startTime = time;
                setTimeText();
                picker_layout.setVisibility(View.INVISIBLE);
                picker_button.setVisibility(View.VISIBLE);
            }
        });
        scrollToTime();

        time_picker_list.setAdapter(timePickerAdapter);
        time_picker_list.setLayoutManager(timePickerLayout);

        time_picker_list.addOnScrollListener(new EndlessScrollListener(timePickerLayout) {
            @Override
            public void onLoadMore(int currentPage) {
                //Insert times
                int cursize = timePickerAdapter.getItemCount();
                timePickerAdapter.addTimes(6);
                timePickerAdapter.notifyItemRangeInserted(cursize, timePickerAdapter.getItemCount());
            }
        });

        myView = v;
        return v;
    }

    private void setTimeText() {
        hour_text.setText("" + ( BasicTime.milToStandardHours((int) startTime.getHours())));
        minute_text.setText(String.format("%1$02d", (startTime.getMinutes() % 60) ));
    }

    private void scrollToTime() {
        int curidx = timePickerAdapter.getTimeIndex(startTime);
        int entries = curidx + 3;
        timePickerAdapter.addTimes(entries - timePickerAdapter.getItemCount() + 1);
        timePickerLayout.scrollToPositionWithOffset(curidx, Util.pixel_to_dp(getContext(), 91 - 13));
    }
    /*
    private void animateTransition() {
        int scale = 1;
        Animation slide_down = new TranslateAnimation(0, 0, 0, Util.pixel_to_dp(getContext(), 160));
        slide_down.setDuration(100 * scale);
        slide_down.setInterpolator(new LinearInterpolator());
        //slide_down.setFillAfter(true);
        slide_arrow.startAnimation(slide_down);

        time_container.setVisibility(View.INVISIBLE);
        picker_layout.setVisibility(View.VISIBLE);
//        slide_down = new TranslateAnimation(0, 0, 0, Util.pixel_to_dp(getContext(), 80));
//        slide_down.setDuration(100 * scale);
//        slide_down.setInterpolator(new LinearInterpolator());
        //slide_down.setFillAfter(true);
        slide_down.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                slide_arrow.setVisibility(View.INVISIBLE);
                time_container.setVisibility(View.INVISIBLE);
                picker_layout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //time_container.startAnimation(slide_down);
    } */
}
