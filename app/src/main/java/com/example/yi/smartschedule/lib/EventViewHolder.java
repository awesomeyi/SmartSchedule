package com.example.yi.smartschedule.lib;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yi.smartschedule.R;
import com.example.yi.smartschedule.TimeViewActivity;

/**
 * Created by Yi on 7/13/16.
 */
public class EventViewHolder extends RecyclerView.ViewHolder {

    public static class EventBlock {
        public EventData event;
        public boolean visible;
        public int height;

        public EventBlock(EventData event, boolean visible) {
            this.event = event;
            this.visible = visible;
            this.height = (int) Math.round(TimeViewActivity.L_BLOCK_HEIGHT * event.getDuration().getHours());
        }

        public EventBlock(Time duration, boolean visible) {
            this(new EventData(null, duration, null, null), visible);
        }

        public EventBlock(int height) {
            this.event = null;
            this.height = height;
            this.visible = false;
        }
        public int getHeight() {
            return this.height;
        }
        public EventBlock setHeight(int height) {
            this.height = height;
            return this;
        }
    }

    //Top level view
    private View my_view;

    //Elements
    private TextView title_text;
    private TextView duration_text;
    private TextView description_text;
    private ImageView event_icon;
    private View event_icon_wrapper;

    public EventViewHolder(View v) {
        super(v);
        this.my_view = v;

        //Init elements
        title_text = ((TextView) v.findViewById(R.id.title_text));
        duration_text = ((TextView) v.findViewById(R.id.duration_text));
        description_text = ((TextView) v.findViewById(R.id.desciption_text));
        event_icon = (ImageView) v.findViewById(R.id.event_icon);
        event_icon_wrapper = v.findViewById(R.id.event_icon_wrapper);
    }

    private String durationText(Time start, Time end) {
        return String.format("%1$s - %2$s", start.formatStandard(), end.formatStandard());
    }

    public void setData(Context ctx, EventBlock eb) {
        //Set dimensions
        int finalHeight = eb.getHeight();
        Util.d("" + Util.pixel_to_dp(ctx, finalHeight));
        my_view.getLayoutParams().height = Util.pixel_to_dp(ctx, finalHeight);

        //Invisible / don't set text
        if(!eb.visible) {
            my_view.setVisibility(View.INVISIBLE);
            return;
        }

        my_view.setVisibility(View.VISIBLE);

        EventData event = eb.event;
        title_text.setText(event.getTitle());
        Time endtime = event.getStartTime().addTime(event.getDuration());
        duration_text.setText(this.durationText(event.getStartTime(), endtime));
        description_text.setText(event.getDescription());
        this.resize(finalHeight);
    }

    private void resize(int height) {

        // >= 2 hours
        if(height >= 2 * blockHeight()) {
            int dTextLine = (height - 2 * blockHeight()) / 20 + 1;
            description_text.setMaxLines(dTextLine);
            return;
        }

        //1 - 2 hours
        description_text.setVisibility(View.GONE);
        if(height >= 1 * blockHeight()) {
            return;
        }

        //.5 - 1 hours
        duration_text.setVisibility(View.GONE);
        event_icon.getLayoutParams().width = event_icon.getLayoutParams().height;
        if(height >= .5 * blockHeight()) {
            return;
        }

        // .25 - .5 hours
        event_icon_wrapper.setVisibility(View.GONE);
        title_text.setText("• • •");
        title_text.setTextSize(height - 8); //4 dp padding
        if(height >= .25 * blockHeight()) {
            return;
        }

        // < .25 hours
        title_text.setVisibility(View.GONE);
    }

    private int blockHeight() {
        return TimeViewActivity.L_BLOCK_HEIGHT;
    }
}
