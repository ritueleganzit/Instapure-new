package com.eleganzit.instapure.utils;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class InfiniteScrollListener extends RecyclerView.OnScrollListener {

    private final static int VISIBLE_THRESHOLD = 2;
    private LinearLayoutManager linearLayoutManager;
    private boolean loading; // LOAD MORE Progress dialog
    private OnLoadMoreListener listener;
    private boolean pauseListening = false;


    private boolean END_OF_FEED_ADDED = false;
    private int NUM_LOAD_ITEMS = 10;

    public InfiniteScrollListener(LinearLayoutManager linearLayoutManager, OnLoadMoreListener listener) {
        this.linearLayoutManager = linearLayoutManager;
        this.listener = listener;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (dx == 0 && dy == 0)
            return;
        int totalItemCount = linearLayoutManager.getItemCount();
        int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

        Log.d("atsudytasyudtasyd","in on scrolled 2    "+!loading +"  "+ (totalItemCount <= lastVisibleItem + VISIBLE_THRESHOLD )+" "+ (totalItemCount != 0 )+"  "+ (!END_OF_FEED_ADDED) +"  "+ (!pauseListening));

        if (!loading && totalItemCount <= lastVisibleItem + VISIBLE_THRESHOLD && totalItemCount != 0 && !END_OF_FEED_ADDED && !pauseListening) {

            Log.d("atsudytasyudtasyd","in on scrolled  first iffff");

            if (listener != null) {
                Log.d("atsudytasyudtasyd","in on scrolled second ifffff");
                listener.onLoadMore();
            }
            loading = true;
        }
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        Log.d("atsudytasyudtasyd","in onScrollStateChanged");
    }

    public void setLoaded() {
        loading = false;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void addEndOfRequests() {
        this.END_OF_FEED_ADDED = true;
    }

    public void pauseScrollListener(boolean pauseListening) {
        this.pauseListening = pauseListening;
    }
}