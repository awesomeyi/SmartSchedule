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
        time_picker_list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        TimePickerAdapter timePickerAdapter = new TimePickerAdapter(new BasicTime(10, 30));
        time_picker_list.setAdapter(timePickerAdapter);
        return v;
    }
}
