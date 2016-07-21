package com.example.yi.smartschedule.lib;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yi.smartschedule.R;
import com.example.yi.smartschedule.TimeViewActivity;

import java.util.ArrayList;

/**
 * Created by Yi on 7/13/16.
 */
public class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {

    private Context context;
    private EventStore allEvents;
    private ArrayList<EventBlock> allBlocks = new ArrayList<>();

    //Minimum gap between elements
    public static double getMinGap() {
        return 1.5; //dp
    }

    public EventAdapter(EventStore allEvents) {
        this.allEvents = allEvents;

        //Insert start padding
        allBlocks.add(EventBlock.create().setHeight(15));

        int dpcount = 0; //Keep a running count of DP
        for(int i = 0; i < allEvents.count(); ++i) {

            EventData event = allEvents.at(i);
            EventBlock eventBlock = EventBlock.create().setVisibility(true).setHeight(event).setEvent(event);
            allBlocks.add(eventBlock); //Add the actual event
            dpcount += eventBlock.getHeight();
            Util.d("Total dp: " + dpcount + " event: " + eventBlock.getHeight());

            //Add the padding
            EventData before = allEvents.at(i), after = allEvents.at(i + 1);
            Time duration;
            EventBlock paddingBlock = EventBlock.create();

            //Last event
            if(after == null) {
                duration = ( new Time((int) Math.ceil(before.getEndTime().getHours()), 0) ).subtractTime(before.getEndTime());
                paddingBlock.setHeight(duration);
            } else {
                duration = after.getStartTime().subtractTime(before.getStartTime().addTime(before.getDuration()));
                paddingBlock.setHeight(duration);

                //Adjust padding so its exact
                if(after.getStartTime().getMinutes() % 30 == 0) {

                    //We fuzz fdp into a multiple of L_BLOCK_HEIGHT / 2
                    int fdp = dpcount + (int) paddingBlock.getHeight();
                    int halfBlock = TimeViewActivity.HALF_HOUR_HEIGHT();
                    double scale = (double) fdp / halfBlock;
                    scale = Math.round(scale);
                    int adjusted = (int) (scale * halfBlock);

                    paddingBlock.setHeight(adjusted - dpcount);
                }
            }
            dpcount += paddingBlock.getHeight();
            allBlocks.add(paddingBlock);
            Util.d("Total dp: " + dpcount + " padding: " + paddingBlock.getHeight());
        }

        //Insert end padding
        allBlocks.add(EventBlock.create().setHeight(15));

        for(int i = 1; i < allBlocks.size() - 1; ++i) {
            EventBlock block = allBlocks.get(i);
            if(!block.getVisibility() && block.getHeight() == 0) {
                EventBlock before = allBlocks.get(i - 1), after = allBlocks.get(i + 1);
                allBlocks.get(i - 1).setHeight(before.getHeight() - getMinGap());
                allBlocks.get(i).setHeight(getMinGap() * 2);
                allBlocks.get(i + 1).setHeight(after.getHeight() - getMinGap());
            }
        }
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
