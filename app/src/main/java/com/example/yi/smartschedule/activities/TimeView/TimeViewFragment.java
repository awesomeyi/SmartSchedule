package com.example.yi.smartschedule.activities.TimeView;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yi.smartschedule.R;
import com.example.yi.smartschedule.lib.BasicTime;
import com.example.yi.smartschedule.lib.EndlessScrollListener;
import com.example.yi.smartschedule.lib.PresetIcon;
import com.example.yi.smartschedule.models.DateManager;
import com.example.yi.smartschedule.models.EventData;
import com.example.yi.smartschedule.models.EventStore;

import aligningrecyclerview.AligningRecyclerView;
import aligningrecyclerview.AlignmentManager;

public class TimeViewFragment extends Fragment {

    private TimeViewListener myListener;

    public TimeViewFragment() {
        // Required empty public constructor
    }

    public static TimeViewFragment newInstance(String param1, String param2) {
        TimeViewFragment fragment = new TimeViewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View my_view = inflater.inflate(R.layout.fragment_time_view, container, false);

        AligningRecyclerView time_line_list = (AligningRecyclerView) my_view.findViewById(R.id.time_line_list);
        AligningRecyclerView main_event_list = (AligningRecyclerView) my_view.findViewById(R.id.main_event_list);

        EventData events[] = {
                new EventData( new BasicTime(7, 00), new BasicTime(0, 60), "Drive to work", "", new PresetIcon(PresetIcon.IC_CAR_EVENT) ),
                new EventData( new BasicTime(9, 00), new BasicTime(0, 60), "Meeting with Joe", "", new PresetIcon(PresetIcon.IC_MEETING_EVENT) ),
                new EventData( new BasicTime(10, 30), new BasicTime(1, 30), "Work on report", "Check out the group outline on google docs", new PresetIcon(PresetIcon.IC_OFFICE_EVENT)),
                new EventData( new BasicTime(13, 00), new BasicTime(0, 60), "Lunch with Donna", "", new PresetIcon(PresetIcon.IC_FOOD_EVENT)),
                new EventData( new BasicTime(14, 00), new BasicTime(0, 60), "Walk with Jane", "", new PresetIcon(PresetIcon.IC_WALKING_EVENT)),
                new EventData( new BasicTime(15, 30), new BasicTime(3, 00), "Finish Article", "Talk to Chuck about section ii and whether it works better later in the piece, and finish conclusion and citations on page 4", new PresetIcon(PresetIcon.IC_WRITING_EVENT)),
                new EventData( new BasicTime(19, 00), new BasicTime(0, 60), "Dinner with John", "", new PresetIcon(PresetIcon.IC_CULTERY_EVENT)),
                new EventData( new BasicTime(20, 30), new BasicTime(0, 60), "Watch Game of Thrones", "", new PresetIcon(PresetIcon.IC_TV_EVENT)),
                new EventData( new BasicTime(21, 30), new BasicTime(0, 60), "Evening workout", "", new PresetIcon(PresetIcon.IC_DUMBELL_EVENT)),
                new EventData( new BasicTime(22, 30), new BasicTime(0, 30), "Read", "", new PresetIcon(PresetIcon.IC_BOOKSHELF_EVENT))
        };

        EventStore eventStore = new EventStore(events);
        final TimeLineAdapter timeLineAdapter = new TimeLineAdapter(eventStore);
        final TimeViewFragment _this = this;
        final EventAdapter eventAdapter = new EventAdapter(eventStore, new EventAdapter.EventHolderClick() {
            @Override
            public void onEventClick(EventData event) {
                _this.onEventClick(event);
            }
        });

        final @AligningRecyclerView.AlignOrientation int orientation;
        orientation = AligningRecyclerView.ALIGN_ORIENTATION_VERTICAL;

        time_line_list.setAdapter(timeLineAdapter);
        LinearLayoutManager timeLineLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        time_line_list.setLayoutManager(timeLineLayout);

        main_event_list.setAdapter(eventAdapter);
        LinearLayoutManager eventLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        main_event_list.setLayoutManager(eventLayout);

        time_line_list.addOnScrollListener(new EndlessScrollListener(timeLineLayout) {
            @Override
            public void onLoadMore(int currentPage) {
                //Insert times
                int cursize = timeLineAdapter.getItemCount();
                timeLineAdapter.addDay(DateManager.getEvents(null), new BasicTime(0, 0));
                timeLineAdapter.notifyItemRangeInserted(cursize, timeLineAdapter.dataCount());

                cursize = eventAdapter.getItemCount();
                eventAdapter.addDay(DateManager.getEvents(null), new BasicTime(0, 0));
                eventAdapter.notifyItemRangeInserted(cursize, eventAdapter.dataCount());
            }
        });

        AlignmentManager.join(orientation, time_line_list, main_event_list);

        return my_view;
    }

    public void onEventClick(EventData event) {
        if (myListener != null) {
            myListener.onEventClick(event);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TimeViewListener) {
            myListener = (TimeViewListener) context;
        } else {
            throw new RuntimeException("Didn't implement listener you dumbass!");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        myListener = null;
    }

    public interface TimeViewListener {
        void onEventClick(EventData event);
    }
}
