package com.example.yi.smartschedule.activities.TimeView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yi.smartschedule.R;
import com.example.yi.smartschedule.activities.MainViewActivity;
import com.example.yi.smartschedule.lib.BasicTime;
import com.example.yi.smartschedule.lib.Util;
import com.example.yi.smartschedule.models.EventStore;

import java.util.ArrayList;

/**
 * Created by Yi on 7/14/16.
 */
public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.ViewHolder> {

    private Context context;
    private ArrayList<EventStore> allEvents = new ArrayList<>();
    private ArrayList<TimeBlock> allTimes = new ArrayList<>();

    private static class TimeBlock {
        public int startMark;
        public int mark;

        public TimeBlock(int startMark, int mark) {
            this.startMark = startMark;
            this.mark = mark;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View my_view;

        public ViewHolder(View v) {
            super(v);
            this.my_view = v;
        }

        public void setData(Context ctx, TimeBlock tb) {
            TextView hour_text = (TextView) my_view.findViewById(R.id.hour_text);
            View hour_view = (my_view.findViewById(R.id.hour_view));
            View hour_mark = my_view.findViewById(R.id.hour_mark);

            int finalheight = Util.pixel_to_dp(ctx, MainViewActivity.FULL_HOUR_HEIGHT());
            my_view.getLayoutParams().height = finalheight;

            hour_text.setText("" + BasicTime.milToStandardHours(tb.startMark));
            switch (tb.mark) {
                case -1:
                    hour_mark.setVisibility(View.INVISIBLE);
                    my_view.getLayoutParams().height = Util.pixel_to_dp(ctx, 30);
                    break;
                case 0:
                    hour_mark.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    hour_mark.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    public TimeLineAdapter(EventStore allEvents) {
        this.addDay(allEvents, allEvents.at(0).getStartTime());
    }

    public TimeLineAdapter addDay(EventStore today, BasicTime starttime) {
        int cur = (int) Math.floor(starttime.getApproxHours());
        int end = 11;

        for(; cur <= end; ++cur) {
            TimeBlock tb = new TimeBlock(cur, 0);
            if( today.startEndInInterval(new BasicTime(cur, 1), new BasicTime(cur, 59)) ) {
                tb.mark = 1;
            }
            allTimes.add(tb);
        }
        return this;
    }

    public int dataCount() {
        return allTimes.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_hour_element, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(context, allTimes.get(position));
    }

    @Override
    public int getItemCount() {
        return allTimes.size();
    }
}
