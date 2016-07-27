package com.example.yi.smartschedule.activities.DetailView;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yi.smartschedule.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimePickerFragment extends Fragment {

    public TimePickerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_time_picker, container, false);
        return v;
    }

}
