package me.vadik.instaclimb.view.adapter.abstr;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import me.vadik.instaclimb.view.adapter.BindingHolder;

/**
 * User: vadik
 * Date: 4/13/16
 */
public abstract class CommonAbstractRecyclerViewAdapter<T> extends RecyclerView.Adapter<BindingHolder> {

    protected final Context mContext;
    protected List<T> mItems;

    public CommonAbstractRecyclerViewAdapter(Context context) {
        this.mContext = context;
        this.mItems = new ArrayList<>();
    }

    public void reset() {
        this.mItems = null;
    }

    public void swap(List<T> items) {
        this.mItems.clear();
        this.mItems.addAll(items);
        this.notifyDataSetChanged();
    }
}