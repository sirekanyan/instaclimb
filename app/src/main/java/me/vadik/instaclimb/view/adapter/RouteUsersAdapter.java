package me.vadik.instaclimb.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import me.vadik.instaclimb.databinding.RowLayoutUserBinding;
import me.vadik.instaclimb.model.User;
import me.vadik.instaclimb.viewmodel.UserItemViewModel;

/**
 * User: vadik
 * Date: 4/13/16
 */
public class RouteUsersAdapter extends RecyclerViewAdapter<User> {

    public RouteUsersAdapter(Context context) {
        super(context);
    }

    @Override
    protected void onBindRowViewHolder(BindingHolder bindingHolder, User user) {
        RowLayoutUserBinding rowUserBinding = (RowLayoutUserBinding) bindingHolder.getBinding();
        rowUserBinding.setUser(new UserItemViewModel(context, user));
        rowUserBinding.executePendingBindings();
    }

    @Override
    protected BindingHolder onCreateRowViewHolder(LayoutInflater inflater, ViewGroup parent) {
        RowLayoutUserBinding rowUserBinding = RowLayoutUserBinding.inflate(inflater, parent, false);
        return new BindingHolder<>(rowUserBinding.getRoot());
    }
}