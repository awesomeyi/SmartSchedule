package com.example.yi.smartschedule.activities.DetailView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yi.smartschedule.R;
import com.example.yi.smartschedule.lib.BasicTime;
import com.example.yi.smartschedule.lib.Util;

import java.util.ArrayList;

/**
 * Created by Yi on 7/27/16.
 */
public class TimePickerAdapter extends RecyclerView.Adapter<TimePickerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<BasicTime> allTimes = new ArrayList<>();
    private TimeSelectListener myListener;
    private int interval = 5;

    public static interface TimeSelectListener {
        public void onTimeClick(BasicTime time);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View myView;
        private BasicTime myTime;
        private TimeSelectListener myListener;

        public ViewHolder(View v, TimeSelectListener myListener) {
            super(v);
            v.setOnClickListener(this);
            this.myView = v;
            this.myListener = myListener;
        }

        public void setData(BasicTime time) {
            this.myTime = time;

            TextView hour_text = (TextView) myView.findViewById(R.id.hour_text),
                    minutes_text = (TextView) myView.findViewById(R.id.minute_text);
            hour_text.setText(time.formatStandard().hour);
            if(time.getTotalMinutes() % 15 == 0) {
                hour_text.setVisibility(View.VISIBLE);
            } else {
                hour_text.setVisibility(View.INVISIBLE);
            }
            minutes_text.setText(time.formatStandard().minute);
        }

        @Override
        public void onClick(View view) {
            myListener.onTimeClick(myTime);
        }
    }

    public TimePickerAdapter(BasicTime startTime, boolean reverse, TimeSelectListener myListener) {
        allTimes.add(startTime);
        this.myListener = myListener;
        if(reverse)
            interval = -interval;
        this.addTimes(6);
    }

    public void addTimes(int tnum) {
        BasicTime start = allTimes.get(allTimes.size() - 1);
        for(int i = 1; i <= tnum; ++i) {
            allTimes.add(BasicTime.create(0, interval * i).addTime(start));
        }
    }

    public int getTimeIndex(BasicTime time) {
        return (time.getTotalMinutes() - allTimes.get(0).getTotalMinutes()) / interval;
    }

    public BasicTime getTimeAt(int idx) {
        if(idx >= allTimes.size() || idx < 0)
            return null;
        return allTimes.get(idx);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_hm_element, parent, false);
        return new ViewHolder(v, new TimeSelectListener() {
            @Override
            public void onTimeClick(BasicTime time) {
                myListener.onTimeClick(time);
            }
        });
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(allTimes.get(position));
    }

    @Override
    public int getItemCount() {
        return allTimes.size();
    }
}
