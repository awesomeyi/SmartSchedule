package com.example.yi.smartschedule.activities.DetailView;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yi.smartschedule.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PresetPickerFragment extends Fragment {

    private RecyclerView preset_picker_list;
    private PresetPickerAdapter presetPickerAdapter;

    public PresetPickerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_preset_picker, container, false);

        preset_picker_list = (RecyclerView) v.findViewById(R.id.preset_picker_list);
        presetPickerAdapter = new PresetPickerAdapter();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        preset_picker_list.setAdapter(presetPickerAdapter);
        preset_picker_list.setLayoutManager(linearLayoutManager);
        return v;
    }

}
