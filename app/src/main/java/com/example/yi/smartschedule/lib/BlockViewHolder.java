package com.example.yi.smartschedule.lib;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.example.yi.smartschedule.TimeViewActivity;

/**
 * Created by Yi on 7/13/16.
 */
public class BlockViewHolder extends RecyclerView.ViewHolder {

    //Top level view
    private View my_view;

    public BlockViewHolder(View v) {
        super(v);
        this.my_view = v;
    }

    public void setData(Context ctx, Time duration) {
        int finalHeight = (int) Math.round(blockHeight() * duration.getHours());
        my_view.getLayoutParams().height = Util.pixel_to_dp(ctx, finalHeight);
    }

    public int blockHeight() {
        return TimeViewActivity.L_BLOCK_HEIGHT;
    }
}
