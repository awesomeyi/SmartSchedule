package com.example.yi.smartschedule.lib;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yi.smartschedule.R;

/**
 * Created by Yi on 7/13/16.
 */
public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int EVENT_ELEMENT = 0;
    private static final int BLOCK_ELEMENT = 1;

    private Context context;
    private EventData allEvents [];

    public static final int L_BLOCK_HEIGHT = 70;

    public EventAdapter(EventData allEvents []) {
        this.allEvents = allEvents;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View v;

        switch (viewType) {
            case EVENT_ELEMENT:
                v = LayoutInflater .from(parent.getContext())
                        .inflate(R.layout.single_event_element, parent, false);
                EventViewHolder evh = new EventViewHolder(v);
                return evh;
            case BLOCK_ELEMENT:
                v = LayoutInflater .from(parent.getContext())
                        .inflate(R.layout.event_block_element, parent, false);
                BlockViewHolder bvh = new BlockViewHolder(v);
                return bvh;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Util.d("" + position);
        switch (getItemViewType(position)) {
            case EVENT_ELEMENT:
                EventViewHolder evh = (EventViewHolder) holder;
                evh.setData(context, allEvents[position / 2]);
                break;
            case BLOCK_ELEMENT:
                BlockViewHolder bvh = (BlockViewHolder) holder;
                int here = position / 2;
                EventData before = allEvents[here], after = allEvents[here + 1];
                Time duration = after.getStartTime().subtractTime( before.getStartTime().addTime(before.getDuration())  );
                bvh.setData(context, duration);
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        switch (position % 2) {
            case 0: return EVENT_ELEMENT;
            case 1: return BLOCK_ELEMENT;
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return allEvents.length * 2 - 1;
    }
}
