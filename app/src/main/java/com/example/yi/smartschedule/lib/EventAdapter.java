package com.example.yi.smartschedule.lib;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yi.smartschedule.R;
import com.example.yi.smartschedule.SingleEventElement;

/**
 * Created by Yi on 7/13/16.
 */
public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private EventData allEvents [];

    public EventAdapter(EventData allEvents []) {
        this.allEvents = allEvents;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater .from(parent.getContext())
                                .inflate(R.layout.fragment_single_event_element, parent, false);
        this.context = parent.getContext();
        EventViewHolder vh = new EventViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        EventViewHolder evh = (EventViewHolder) holder;
        evh.setText(context, allEvents[position]);
    }

    @Override
    public int getItemCount() {
        return allEvents.length;
    }
}
