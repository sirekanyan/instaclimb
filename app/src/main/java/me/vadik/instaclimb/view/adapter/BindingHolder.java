package me.vadik.instaclimb.view.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * User: vadik
 * Date: 5/6/16
 */
public class BindingHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {
    final T binding;

    public BindingHolder(View view) {
        super(view);
        this.binding = DataBindingUtil.bind(view);
    }

    public T getBinding() {
        return binding;
    }
}

