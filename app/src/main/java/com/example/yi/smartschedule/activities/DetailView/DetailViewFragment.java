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

    public static DetailViewFragment newInstance() {
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

        TimePickerFragment top_time_picker = TimePickerFragment.newInstance(new BasicTime(10, 30), new BasicTime(12, 00), true);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.top_picker_container, top_time_picker)
                    .commit();
        }
        return v;
    }
}
