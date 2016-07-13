package com.example.yi.smartschedule.lib;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yi.smartschedule.R;

/**
 * Created by Yi on 7/13/16.
 */
public class EventViewHolder extends RecyclerView.ViewHolder {

    private static final int L_HOUR_HEIGHT = 70;

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

    public void setText(Context ctx, EventData ed) {
        title_text.setText(ed.getTitle());
        Time endtime = ed.getStartTime().addTime(ed.getDuration());
        duration_text.setText(this.durationText(ed.getStartTime(), endtime));
        description_text.setText(ed.getDescription());

        int finalHeight = (int) Math.round(L_HOUR_HEIGHT * ed.getDuration().getHours());
        my_view.getLayoutParams().height = Util.pixel_to_dp(ctx, finalHeight);
        this.resize(finalHeight);
    }

    private void resize(int height) {

        // >= 2 hours
        if(height >= 2 * L_HOUR_HEIGHT) {
            int dTextLine = (height - 2 * L_HOUR_HEIGHT) / 20 + 1;
            description_text.setMaxLines(dTextLine);
            return;
        }

        //1 - 2 hours
        description_text.setVisibility(View.GONE);
        if(height >= 1 * L_HOUR_HEIGHT) {
            return;
        }

        //.5 - 1 hours
        duration_text.setVisibility(View.GONE);
        event_icon.getLayoutParams().width = event_icon.getLayoutParams().height;
        if(height >= .5 * L_HOUR_HEIGHT) {
            return;
        }

        // .25 - .5 hours
        event_icon_wrapper.setVisibility(View.GONE);
        title_text.setText("• • •");
        title_text.setTextSize(height - 8); //4 dp padding
        if(height >= .25 * L_HOUR_HEIGHT) {
            return;
        }

        // < .25 hours
        title_text.setVisibility(View.GONE);
    }
}
