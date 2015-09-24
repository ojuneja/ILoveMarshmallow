package com.zappos.ojasjuneja.ilovemarshmallow.utils;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;


public abstract class EndlessRecyclerView extends RecyclerView.OnScrollListener {
    public static String TAG = EndlessRecyclerView.class.getSimpleName();

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 5; // The minimum amount of items to have below your current scroll position before loading more.
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private int current_page = 1;

    private GridLayoutManager gridLayoutManager;

    public EndlessRecyclerView(GridLayoutManager gridLayoutManager) {
        this.gridLayoutManager = gridLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = gridLayoutManager.getItemCount();
        previousTotal = previousTotal + visibleItemCount;
        if(previousTotal>totalItemCount)
        {
            current_page++;
            onLoadMore(current_page);
            previousTotal = 0;
        }
    }



    public abstract void onLoadMore(int current_page);
}