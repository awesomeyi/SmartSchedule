package com.example.yi.smartschedule.lib;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Yi on 7/22/16.
 */
public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {
    private int visibleThreshold = 5;
    private int previousItems = 0;
    private boolean loading = true;

    private int currentPage = 0;

    LinearLayoutManager layoutManager;

    public EndlessScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
        int totalItems = layoutManager.getItemCount();

        if(loading && previousItems < totalItems) {
            loading = false;
            previousItems = totalItems;
        }

        if(!loading && lastVisibleItem + visibleThreshold > totalItems) {
            onLoadMore(currentPage);
            loading = true;
        }
    }

    public abstract void onLoadMore(int currentPage);
}
