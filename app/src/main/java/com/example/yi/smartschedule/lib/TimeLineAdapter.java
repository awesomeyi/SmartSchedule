package com.example.yi.smartschedule.lib;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yi.smartschedule.R;
import com.example.yi.smartschedule.TimeViewActivity;

import java.util.ArrayList;

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

            hour_text.setText("" + tb.startMark);
            Util.d("" + Util.pixel_to_dp(ctx, TimeViewActivity.L_BLOCK_HEIGHT) + ", " + hour_text.getLineHeight());
            hour_view.getLayoutParams().height = Util.pixel_to_dp(ctx, TimeViewActivity.L_BLOCK_HEIGHT) - hour_text.getLineHeight();
            switch (tb.mark) {
                case -1:
                    hour_view.getLayoutParams().height = Util.pixel_to_dp(ctx, 0);
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
