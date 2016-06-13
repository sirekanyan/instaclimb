package me.vadik.instaclimb.view.adapter.abstr;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import me.vadik.instaclimb.view.adapter.BindingHolder;

/**
 * User: vadik
 * Date: 6/9/16
 */
public abstract class AbstractRecyclerViewWithHeaderAdapter<H, I> extends CommonAbstractRecyclerViewAdapter<I> {

    private H headerObject;
    private static final int ITEM_TYPE = 0;
    private static final int HEADER_TYPE = 1;

    public AbstractRecyclerViewWithHeaderAdapter(Context context, H headerObject) {
        super(context);
        this.headerObject = headerObject;
    }

    public void setHeaderObject(H headerObject) {
        this.headerObject = headerObject;
        notifyItemChanged(0);
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding;
        if (viewType == HEADER_TYPE) {
            binding = onCreateHeader(inflater, parent);
        } else {
            binding = onCreateItem(inflater, parent);
        }
        return new BindingHolder<>(binding);
    }

    protected abstract ViewDataBinding onCreateHeader(LayoutInflater inflater, ViewGroup parent);

    protected abstract ViewDataBinding onCreateItem(LayoutInflater inflater, ViewGroup parent);

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        ViewDataBinding binding = holder.getBinding();
        if (isHeaderPosition(position)) {
            onBindHeader(binding, headerObject);
        } else {
            onBindItem(binding, mItems.get(position - 1));
        }
        binding.executePendingBindings();
    }

    protected abstract void onBindHeader(ViewDataBinding binding, H header);

    protected abstract void onBindItem(ViewDataBinding binding, I item);

    @Override
    public int getItemCount() {
        return mItems.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderPosition(position)) {
            return HEADER_TYPE;
        } else {
            return ITEM_TYPE;
        }
    }

    private boolean isHeaderPosition(int position) {
        return position == 0;
    }
}
