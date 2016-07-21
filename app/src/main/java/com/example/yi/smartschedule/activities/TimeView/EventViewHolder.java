package com.example.yi.smartschedule.activities.TimeView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yi.smartschedule.R;
import com.example.yi.smartschedule.activities.TimeView.EventAdapter;
import com.example.yi.smartschedule.activities.TimeViewActivity;
import com.example.yi.smartschedule.lib.BasicTime;
import com.example.yi.smartschedule.lib.Util;
import com.example.yi.smartschedule.models.EventData;
import com.example.yi.smartschedule.models.TimeView.EventBlock;

/**
 * Created by Yi on 7/13/16.
 */
public class EventViewHolder extends RecyclerView.ViewHolder {

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

    private String durationText(BasicTime start, BasicTime end) {
        return String.format("%1$s - %2$s", start.formatStandard(), end.formatStandard());
    }

    public void setData(Context ctx, EventBlock eb) {
        //Set dimensions
        int finalHeight = (int) eb.getHeight();
        //Util.d("" + Util.pixel_to_dp(ctx, finalHeight));
        my_view.getLayoutParams().height = Util.pixel_to_dp(ctx, finalHeight);

        //Invisible / don't set text
        if(!eb.getVisibility()) {
            my_view.setVisibility(View.INVISIBLE);
            return;
        }

        //Set everything to visible
        my_view.setVisibility(View.VISIBLE);
        title_text.setVisibility(View.VISIBLE);
        duration_text.setVisibility(View.VISIBLE);
        description_text.setVisibility(View.VISIBLE);

        //Set actual event data
        EventData event = eb.getEvent();
        title_text.setText(event.getTitle());
        BasicTime endtime = event.getStartTime().addTime(event.getDuration());
        duration_text.setText(this.durationText(event.getStartTime(), endtime));
        description_text.setText(event.getDescription());
        event_icon.setImageResource(event.getIcon().getIconId(ctx) );
        this.resize(finalHeight);
    }

    private void resize(int height) {

        // >= 2 hours
        if(height >= 2 * blockHeight() - minGap()) {
            int dTextLine = (height - 2 * blockHeight()) / 20 + 1;
            description_text.setMaxLines(dTextLine);
            return;
        }

        //1 - 2 hours
        description_text.setVisibility(View.GONE);
        if(height >= 1 * blockHeight() - minGap()) {
            return;
        }

        //.5 - 1 hours
        duration_text.setVisibility(View.GONE);
        event_icon.getLayoutParams().width = event_icon.getLayoutParams().height;
        if(height >= .5 * blockHeight() - minGap()) {
            return;
        }

        // .25 - .5 hours
        event_icon_wrapper.setVisibility(View.GONE);
        title_text.setText("• • •");
        title_text.setTextSize(height - 8); //4 dp padding
        if(height >= .25 * blockHeight() - minGap()) {
            return;
        }

        // < .25 hours
        title_text.setVisibility(View.GONE);
    }

    private int blockHeight() {
        return TimeViewActivity.FULL_HOUR_HEIGHT();
    }
    private double minGap() {
        return 2 * EventAdapter.getMinGap();
    }
}
