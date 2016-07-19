package com.example.yi.smartschedule.lib;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yi.smartschedule.R;

import java.util.ArrayList;

/**
 * Created by Yi on 7/13/16.
 */
public class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {

    private static final int EVENT_ELEMENT = 0;
    private static final int BLOCK_ELEMENT = 1;

    private Context context;
    private EventStore allEvents;
    private ArrayList<EventViewHolder.EventBlock> allBlocks = new ArrayList<>();

    public EventAdapter(EventStore allEvents) {
        this.allEvents = allEvents;

        //Insert start padding
        allBlocks.add(new EventViewHolder.EventBlock(15));

        for(int i = 0; i < allEvents.count() - 1; ++i) {
            EventData event = allEvents.at(i);
            allBlocks.add(new EventViewHolder.EventBlock(event, true)); //Add the actual event

            //Add the padding
            EventData before = allEvents.at(i), after = allEvents.at(i + 1);
            Time duration = after.getStartTime().subtractTime( before.getStartTime().addTime(before.getDuration())  );
            allBlocks.add(new EventViewHolder.EventBlock(duration, false));
        }
        //Insert last event
        EventData last = allEvents.last();
        allBlocks.add(new EventViewHolder.EventBlock(last, true));
        //End padding
        Time duration = ( new Time((int) Math.ceil(last.getEndTime().getHours()), 0) ).subtractTime(last.getEndTime());
        allBlocks.add(new EventViewHolder.EventBlock(duration, false));
        allBlocks.add(new EventViewHolder.EventBlock(15));
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
