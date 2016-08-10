package com.example.yi.smartschedule.activities.DetailView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.yi.smartschedule.R;
import com.example.yi.smartschedule.lib.PresetIcon;
import com.example.yi.smartschedule.lib.Util;

import java.util.ArrayList;

/**
 * Created by Yi on 8/10/16.
 */
public class PresetPickerAdapter extends RecyclerView.Adapter<PresetPickerAdapter.ViewHolder>  {

    private Context context;
    private ArrayList<PresetIcon> allIcons = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View myView;

        public ViewHolder(View v) {
            super(v);
            this.myView = v;
        }

        public void setData(Context ctx, PresetIcon icon) {
            ImageView picker_icon = (ImageView) myView.findViewById(R.id.picker_icon);
            picker_icon.setImageResource(icon.getIconId(ctx));
        }
    }

    public PresetPickerAdapter() {
        allIcons.add(new PresetIcon(PresetIcon.IC_BOOKSHELF_EVENT));
        allIcons.add(new PresetIcon(PresetIcon.IC_TV_EVENT));
        allIcons.add(new PresetIcon(PresetIcon.IC_WRITING_EVENT));
        allIcons.add(new PresetIcon(PresetIcon.IC_CAR_EVENT));
        allIcons.add(new PresetIcon(PresetIcon.IC_CULTERY_EVENT));
        allIcons.add(new PresetIcon(PresetIcon.IC_DUMBELL_EVENT));
        allIcons.add(new PresetIcon(PresetIcon.IC_FOOD_EVENT));
        allIcons.add(new PresetIcon(PresetIcon.IC_BOOKSHELF_EVENT));
        allIcons.add(new PresetIcon(PresetIcon.IC_TV_EVENT));
        allIcons.add(new PresetIcon(PresetIcon.IC_WRITING_EVENT));
        allIcons.add(new PresetIcon(PresetIcon.IC_CAR_EVENT));
        allIcons.add(new PresetIcon(PresetIcon.IC_CULTERY_EVENT));
        allIcons.add(new PresetIcon(PresetIcon.IC_DUMBELL_EVENT));
        allIcons.add(new PresetIcon(PresetIcon.IC_FOOD_EVENT));
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_picker_element, parent, false);
        return new ViewHolder(v);
    }

    //50dp large
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(context, allIcons.get(position));
    }

    @Override
    public int getItemCount() {
        return allIcons.size();
    }

}
