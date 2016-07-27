package com.example.yi.smartschedule.activities.DetailView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yi.smartschedule.R;
import com.example.yi.smartschedule.lib.BasicTime;

import java.util.ArrayList;

/**
 * Created by Yi on 7/27/16.
 */
public class TimePickerAdapter extends RecyclerView.Adapter<TimePickerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<BasicTime> allTimes = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View myView;

        public ViewHolder(View v) {
            super(v);
            this.myView = v;
        }

        public void setData(BasicTime time) {
            TextView hour_text = (TextView) myView.findViewById(R.id.hour_text),
                    minutes_text = (TextView) myView.findViewById(R.id.minute_text);
            hour_text.setText("" + ( BasicTime.milToStandardHours((int) time.getHours())));
            if(time.getMinutes() % 15 == 0) {
                hour_text.setVisibility(View.VISIBLE);
            } else {
                hour_text.setVisibility(View.INVISIBLE);
            }
            minutes_text.setText(String.format("%1$02d", (time.getMinutes() % 60) ));
        }
    }

    public TimePickerAdapter(BasicTime startTime) {
        allTimes.add(startTime);
        this.addTimes(6);
    }

    public void addTimes(int tnum) {
        BasicTime start = allTimes.get(allTimes.size() - 1);
        for(int i = 1; i <= tnum; ++i) {
            allTimes.add((new BasicTime(0, 5 * i).addTime(start)));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_hm_element, parent, false);
        return new ViewHolder(v);
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
