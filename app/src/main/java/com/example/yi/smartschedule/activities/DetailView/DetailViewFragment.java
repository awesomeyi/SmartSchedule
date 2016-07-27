package com.example.yi.smartschedule.activities.DetailView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yi.smartschedule.R;
import com.example.yi.smartschedule.lib.BasicTime;
import com.example.yi.smartschedule.lib.EndlessScrollListener;
import com.example.yi.smartschedule.models.DateManager;

public class DetailViewFragment extends Fragment {

    public DetailViewFragment() { }

    public static DetailViewFragment newInstance(String param1, String param2) {
        DetailViewFragment fragment = new DetailViewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detail_view, container, false);
        RecyclerView time_picker_list = (RecyclerView) v.findViewById(R.id.time_picker_list);

        final LinearLayoutManager timePickerLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        final TimePickerAdapter timePickerAdapter = new TimePickerAdapter(new BasicTime(10, 30));

        time_picker_list.setAdapter(timePickerAdapter);
        time_picker_list.setLayoutManager(timePickerLayout);

        time_picker_list.addOnScrollListener(new EndlessScrollListener(timePickerLayout) {
            @Override
            public void onLoadMore(int currentPage) {
                //Insert times
                int cursize = timePickerAdapter.getItemCount();
                timePickerAdapter.addTimes(6);
                timePickerAdapter.notifyItemRangeInserted(cursize, timePickerAdapter.getItemCount());
            }
        });
        return v;
    }
}
