package com.example.yi.smartschedule.lib;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yi.smartschedule.R;
import com.example.yi.smartschedule.TimeViewActivity;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Yi on 7/14/16.
 */
public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.ViewHolder> {

    private Context context;
    private EventStore allEvents;
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
            //Random r = new Random();
            //my_view.setBackgroundColor(Color.rgb(r.nextInt(255), r.nextInt(255), r.nextInt(255)));

            int finalheight = Util.pixel_to_dp(ctx, TimeViewActivity.FULL_HOUR_HEIGHT());
            my_view.getLayoutParams().height = finalheight;
            Util.d("" + (hour_text.getLineHeight()));

            hour_text.setText("" + Time.milToStandardHours(tb.startMark));
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
        this.allEvents = allEvents;
        int cur = (int) Math.floor(allEvents.first().getStartTime().getHours());
        int end = (int) Math.ceil(allEvents.last().getEndTime().getHours()) + 1;

        while(cur != end) {
            TimeBlock tb = new TimeBlock(cur, 0);
            if( allEvents.startEndInInterval(new Time(cur, 1), new Time(cur, 59)) ) {
                tb.mark = 1;
            }
            allTimes.add(tb);
            cur += 1;
        }
        allTimes.get(allTimes.size() - 1).mark = -1;
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
