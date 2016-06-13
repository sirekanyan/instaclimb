package me.vadik.instaclimb.view.adapter.abstr;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import me.vadik.instaclimb.view.adapter.BindingHolder;

/**
 * User: vadik
 * Date: 4/13/16
 */
public abstract class AbstractRecyclerViewAdapter<T> extends CommonAbstractRecyclerViewAdapter<T> {

    public AbstractRecyclerViewAdapter(Context context) {
        super(context);
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = onCreateItem(inflater, parent);
        return new BindingHolder<>(binding);
    }

    protected abstract ViewDataBinding onCreateItem(LayoutInflater inflater, ViewGroup parent);

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        ViewDataBinding binding = holder.getBinding();
        onBindItem(binding, mItems.get(position));
        binding.executePendingBindings();
    }

    protected abstract void onBindItem(ViewDataBinding bindingHolder, T object);

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}