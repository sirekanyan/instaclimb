package me.vadik.instaclimb.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * User: vadik
 * Date: 4/13/16
 */
public abstract class RecyclerViewAdapter<T> extends RecyclerView.Adapter<BindingHolder> {
    protected final Context context;
    protected List<T> items;

    public RecyclerViewAdapter(Context context) {
        items = new ArrayList<>();
        this.context = context;
    }

    public void clear() {
        this.items.clear();
    }

    public void swap(List<T> items) {
        this.clear();
        this.items.addAll(items);
        this.notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(BindingHolder bindingHolder, int position) {
        onBindRowViewHolder(bindingHolder, items.get(position));
        bindingHolder.getBinding().executePendingBindings();
    }

    protected abstract void onBindRowViewHolder(BindingHolder bindingHolder, T object);

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return onCreateRowViewHolder(inflater, parent);
    }

    protected abstract BindingHolder onCreateRowViewHolder(LayoutInflater inflater, ViewGroup parent);

    @Override
    public int getItemCount() {
        return items.size();
    }
}