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

    private static final String P_START = "startTime";
    private static final String P_LIMIT = "limit";
    private static final String P_TOP = "top";

    //TimePicker size/height
    private static final int DISPLAY_OFFSET = 3;
    private static final int BLOCK_HEIGHT = 26;
    private static final int LIST_HEIGHT = (DISPLAY_OFFSET * 2 + 1) * 26; //182
    private static final int ANIMATION_DURATION = 200; //ms
    private static final int ARROW_HEIGHT = 10;
    private static final int ARROW_OFFSET = 23;

    private BasicTime startTime, limit;
    private boolean top = true;

    //Views
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

        final int ARROW_END = Math.round((LIST_HEIGHT - ARROW_OFFSET) / 2);

        picker_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(top) {
                    int oy = 0, ny = ARROW_END + getOffset();
                    time_indicator_down.startAnimation(createSlide(oy, ny));
                    time_indicator_up.startAnimation(createSlide(oy - ARROW_HEIGHT, ny - ARROW_HEIGHT));

                } else {
                    int oy = 0, ny = -(ARROW_END + getOffset());
                    time_indicator_up.startAnimation(createSlide(oy, ny));
                    time_indicator_down.startAnimation(createSlide(oy + ARROW_HEIGHT, ny + ARROW_HEIGHT));
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
                    int oy = ARROW_END + getOffset(), ny = 0;
                    time_indicator_down.startAnimation(createSlide(oy, ny));
                    time_indicator_up.startAnimation(createSlide(oy - ARROW_HEIGHT, ny - ARROW_HEIGHT));
                } else {
                    int oy = -(ARROW_END + getOffset()), ny = 0;
                    time_indicator_up.startAnimation(createSlide(oy, ny));
                    time_indicator_down.startAnimation(createSlide(oy + ARROW_HEIGHT, ny + ARROW_HEIGHT));
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
        if(curidx <= DISPLAY_OFFSET)
            return BLOCK_HEIGHT * (DISPLAY_OFFSET - curidx);
        return 0;
    }

    private void setTimeText() {
        hour_text.setText(startTime.formatStandard().hour);
        minute_text.setText(startTime.formatStandard().minute);
    }

    private void scrollToTime() {
        int curidx = timePickerAdapter.getTimeIndex(startTime);
        int entries = curidx + DISPLAY_OFFSET;
        timePickerAdapter.addTimes(entries - timePickerAdapter.getItemCount() + 1);
        timePickerLayout.scrollToPositionWithOffset(curidx, Util.pixel_to_dp(getContext(), (LIST_HEIGHT / 2) - (BLOCK_HEIGHT / 2)));
    }

    private Animation createSlide(int oy, int ny) {
        Animation slide = new TranslateAnimation(0, 0, Util.pixel_to_dp(getContext(), oy) , Util.pixel_to_dp(getContext(), ny));
        slide.setDuration(ANIMATION_DURATION);
        slide.setInterpolator(new FastOutSlowInInterpolator());
        slide.setFillAfter(true);
        return slide;
    }

}
