package me.vadik.instaclimb.view.adapter;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import me.vadik.instaclimb.databinding.RowLayoutRouteBinding;
import me.vadik.instaclimb.databinding.UserCardBinding;
import me.vadik.instaclimb.model.Route;
import me.vadik.instaclimb.model.User;
import me.vadik.instaclimb.view.adapter.abstr.AbstractRecyclerViewWithHeaderAdapter;
import me.vadik.instaclimb.viewmodel.RouteItemViewModel;
import me.vadik.instaclimb.viewmodel.UserViewModel;

/**
 * User: vadik
 * Date: 4/13/16
 */
public class UserRecyclerViewAdapter extends AbstractRecyclerViewWithHeaderAdapter<User, Route> {

    public UserRecyclerViewAdapter(Context context, User user) {
        super(context, user);
    }

    @Override
    protected ViewDataBinding onCreateHeader(LayoutInflater inflater, ViewGroup parent) {
        return UserCardBinding.inflate(inflater, parent, false);
    }

    @Override
    protected ViewDataBinding onCreateItem(LayoutInflater inflater, ViewGroup parent) {
        return RowLayoutRouteBinding.inflate(inflater, parent, false);
    }

    @Override
    protected void onBindHeader(ViewDataBinding binding, User user) {
        ((UserCardBinding) binding).setUser(new UserViewModel(mContext, user));
    }

    @Override
    protected void onBindItem(ViewDataBinding binding, Route route) {
        ((RowLayoutRouteBinding) binding).setRoute(new RouteItemViewModel(mContext, route));
    }
}