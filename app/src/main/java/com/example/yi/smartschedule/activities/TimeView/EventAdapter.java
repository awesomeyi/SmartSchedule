package com.example.yi.smartschedule.activities.TimeView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yi.smartschedule.R;
import com.example.yi.smartschedule.activities.TimeViewActivity;
import com.example.yi.smartschedule.lib.BasicTime;
import com.example.yi.smartschedule.lib.Util;
import com.example.yi.smartschedule.models.EventData;
import com.example.yi.smartschedule.models.EventStore;
import com.example.yi.smartschedule.models.TimeView.EventBlock;

import java.util.ArrayList;

/**
 * Created by Yi on 7/13/16.
 */
public class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {

    private Context context;
    private ArrayList<EventBlock> allBlocks = new ArrayList<>();
    private ArrayList<EventStore> allEvents = new ArrayList<>();
    private int dpcount = 0;

    //Minimum gap between elements
    public static double getMinGap() {
        return 1.5; //dp
    }

    public EventAdapter(EventStore allEvents) {
        //Insert start padding
        allBlocks.add(EventBlock.create().setHeight(15));
        this.addDay(allEvents, allEvents.at(0).getStartTime());
    }

    public EventAdapter addDay(EventStore today, BasicTime starttime) {

        //Insert start pad from starttime
        if(today.count() > 0) {
            EventBlock startPad = EventBlock.create();
            EventData start = today.first();
            BasicTime duration = start.getStartTime().subtractTime(starttime);
            startPad.setHeight(duration);

            if(start.getStartTime().getMinutes() % 30 == 0) {
                this.fudgePadding(startPad);
            }
            allBlocks.add(startPad);
        }

        for(int i = 0; i < today.count(); ++i) {

            EventData event = today.at(i);
            EventBlock eventBlock = EventBlock.create().setVisibility(true).setHeight(event).setEvent(event);
            allBlocks.add(eventBlock); //Add the actual event
            dpcount += eventBlock.getHeight();
            //Util.d("Total dp: " + dpcount + " event: " + eventBlock.getHeight());

            //Add the padding
            EventData before = today.at(i), after = today.at(i + 1);
            BasicTime duration;
            EventBlock paddingBlock = EventBlock.create();

            //Last event pad to the last hour
            if(after == null) {
                duration = ( new BasicTime((int) Math.ceil(before.getEndTime().getHours()), 0) ).subtractTime(before.getEndTime());
                paddingBlock.setHeight(duration);
                this.fudgePadding(paddingBlock);
            } else {
                duration = after.getStartTime().subtractTime(before.getEndTime());
                paddingBlock.setHeight(duration);

                //Adjust padding so its exact
                if(after.getStartTime().getMinutes() % 30 == 0) {
                    this.fudgePadding(paddingBlock);
                }
            }
            dpcount += paddingBlock.getHeight();
            allBlocks.add(paddingBlock);
            //Util.d("Total dp: " + dpcount + " padding: " + paddingBlock.getHeight());
        }

        allEvents.add(today);

        //Space out events
        for(int i = 1; i < allBlocks.size() - 1; ++i) {
            EventBlock block = allBlocks.get(i);
            if(!block.getVisibility() && block.getHeight() == 0) {
                EventBlock before = allBlocks.get(i - 1), after = allBlocks.get(i + 1);
                allBlocks.get(i - 1).setHeight(before.getHeight() - getMinGap());
                allBlocks.get(i).setHeight(getMinGap() * 2);
                allBlocks.get(i + 1).setHeight(after.getHeight() - getMinGap());
            }
        }
        return this;
    }

    //Rounding errors can occur, this is why we fudge the padding to an exact multiple of HALF_HOUR_BLOCK()
    private void fudgePadding(EventBlock paddingBlock) {
        int fdp = dpcount + (int) paddingBlock.getHeight();
        int halfBlock = TimeViewActivity.HALF_HOUR_HEIGHT();
        double scale = (double) fdp / halfBlock;
        scale = Math.round(scale);
        int adjusted = (int) (scale * halfBlock);

        paddingBlock.setHeight(adjusted - dpcount);
    }

    public int dataCount() {
        return allBlocks.size();
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View v = LayoutInflater .from(parent.getContext())
                        .inflate(R.layout.single_event_element, parent, false);
        EventViewHolder evh = new EventViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        holder.setData(context, allBlocks.get(position));
    }

    @Override
    public int getItemCount() {
        return allBlocks.size();
    }
}
