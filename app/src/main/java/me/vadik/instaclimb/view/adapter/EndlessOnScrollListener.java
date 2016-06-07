package me.vadik.instaclimb.view.adapter;

import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * User: vadik
 * Date: 6/6/16
 */
//todo!
public abstract class EndlessOnScrollListener implements NestedScrollView.OnScrollChangeListener {

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 1; // The minimum amount of items to have below your current scroll position before loading more.
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private int current_page = 1;

    private final LinearLayoutManager mLinearLayoutManager;
    private final RecyclerView mRecyclerView;

    public EndlessOnScrollListener(LinearLayoutManager linearLayoutManager, RecyclerView recyclerView) {
        this.mLinearLayoutManager = linearLayoutManager;
        this.mRecyclerView = recyclerView;
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        visibleItemCount = mRecyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading && (totalItemCount - visibleThreshold)
                <= (firstVisibleItem + visibleItemCount)) {
            // End has been reached

            // Do something
            current_page++;

            onLoadMore(current_page);

            loading = true;
        }
    }

    public abstract void onLoadMore(int current_page);
}