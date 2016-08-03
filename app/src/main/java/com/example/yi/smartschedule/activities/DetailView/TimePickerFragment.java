package com.example.yi.smartschedule.activities.DetailView;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
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
    private static String P_TOP = "top";

    private BasicTime startTime, limit;
    private boolean top = true;

    private View myView;
    private RecyclerView time_picker_list;
    private View picker_button, picker_layout;
    private TextView hour_text, minute_text;
    private View time_indicator_up, time_indicator_down, top_block;

    private TimePickerAdapter timePickerAdapter;
    private LinearLayoutManager timePickerLayout;


    public static TimePickerFragment newInstance(BasicTime startTime, BasicTime limit, boolean reverse) {
        TimePickerFragment myFragment = new TimePickerFragment();

        Bundle args = new Bundle();
        args.putSerializable(P_START, startTime);
        args.putSerializable(P_LIMIT, limit);
        args.putBoolean(P_TOP, reverse);

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
            top = b.getBoolean(P_TOP);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_time_picker, container, false);

        //Init elements
        picker_layout = v.findViewById(R.id.picker_layout);
        picker_button = v.findViewById(R.id.picker_button);
        time_picker_list = (RecyclerView) v.findViewById(R.id.time_picker_list);

        hour_text = (TextView) v.findViewById(R.id.hour_text);
        minute_text = (TextView) v.findViewById(R.id.minute_text);

        time_indicator_up = v.findViewById(R.id.time_indicator_up);
        time_indicator_down = v.findViewById(R.id.time_indicator_down);
        top_block = v.findViewById(R.id.top_block);


        this.setTimeText();

        picker_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(top) {
                    time_indicator_up.startAnimation(createSlide(0, 0, -10, 70 + getOffset()));
                    time_indicator_down.startAnimation(createSlide(0, 0, 0, 80 + getOffset()));
                } else {
                    time_indicator_up.startAnimation(createSlide(0, 0, 0, -(80 + getOffset()) ));
                    time_indicator_down.startAnimation(createSlide(0, 0, 10, -(70 + getOffset()) ));
                }

                picker_layout.setVisibility(View.VISIBLE);
                picker_button.setVisibility(View.INVISIBLE);
            }
        });

        timePickerLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, top);
        timePickerAdapter = new TimePickerAdapter(limit, top, new TimePickerAdapter.TimeSelectListener() {
            @Override
            public void onTimeClick(BasicTime time) {
                if(top) {
                    time_indicator_up.startAnimation(createSlide(0, 0, 70 + getOffset(), -10));
                    time_indicator_down.startAnimation(createSlide(0, 0, 80 + getOffset(), 0));
                } else {
                    time_indicator_up.startAnimation(createSlide(0, 0, -(80 + getOffset()), 0));
                    time_indicator_down.startAnimation(createSlide(0, 0, -(70 + getOffset()), 10));
                }

                startTime = time;
                setTimeText();
                picker_layout.setVisibility(View.INVISIBLE);
                picker_button.setVisibility(View.VISIBLE);
                scrollToTime();
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

        if(!top) {
            adjustBottom();
        }

        myView = v;
        return v;
    }

    private void adjustBottom() {
        addAlignParentBottom(picker_button);
        addAlignParentBottom(top_block);
        addAlignParentBottom(time_indicator_up);
        addAlignParentBottom(time_indicator_down);
        time_indicator_up.setVisibility(View.VISIBLE);
        time_indicator_down.setVisibility(View.INVISIBLE);
    }

    private void addAlignParentBottom(View v) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        v.setLayoutParams(params);
    }

    private int getOffset() {
        int curidx = timePickerAdapter.getTimeIndex(startTime);
        if(curidx <= 3)
            return 26 * (3 - curidx);
        return 0;
    }

    private void setTimeText() {
        hour_text.setText(startTime.formatStandard().hour);
        minute_text.setText(startTime.formatStandard().minute);
    }

    private void scrollToTime() {
        int curidx = timePickerAdapter.getTimeIndex(startTime);
        int entries = curidx + 3;
        timePickerAdapter.addTimes(entries - timePickerAdapter.getItemCount() + 1);
        timePickerLayout.scrollToPositionWithOffset(curidx, Util.pixel_to_dp(getContext(), 91 - 13));
    }

    private Animation createSlide(int ox, int nx, int oy, int ny) {
        Animation slide = new TranslateAnimation(Util.pixel_to_dp(getContext(), ox), Util.pixel_to_dp(getContext(), nx), Util.pixel_to_dp(getContext(), oy) , Util.pixel_to_dp(getContext(), ny));
        slide.setDuration(200);
        slide.setInterpolator(new FastOutSlowInInterpolator());
        slide.setFillAfter(true);
        return slide;
    }

}
