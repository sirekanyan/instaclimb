package me.vadik.instaclimb.view.adapter;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import me.vadik.instaclimb.databinding.RouteCardBinding;
import me.vadik.instaclimb.databinding.RowLayoutUserBinding;
import me.vadik.instaclimb.model.Route;
import me.vadik.instaclimb.model.User;
import me.vadik.instaclimb.view.adapter.abstr.AbstractRecyclerViewWithHeaderAdapter;
import me.vadik.instaclimb.viewmodel.RouteViewModel;
import me.vadik.instaclimb.viewmodel.UserItemViewModel;

/**
 * User: vadik
 * Date: 4/13/16
 */
public class RouteRecyclerViewAdapter extends AbstractRecyclerViewWithHeaderAdapter<Route, User> {

    public RouteRecyclerViewAdapter(Context context, Route route) {
        super(context, route);
    }

    @Override
    protected ViewDataBinding onCreateHeader(LayoutInflater inflater, ViewGroup parent) {
        return RouteCardBinding.inflate(inflater, parent, false);
    }

    @Override
    protected ViewDataBinding onCreateItem(LayoutInflater inflater, ViewGroup parent) {
        return RowLayoutUserBinding.inflate(inflater, parent, false);
    }

    @Override
    protected void onBindHeader(ViewDataBinding binding, Route route) {
        ((RouteCardBinding) binding).setRoute(new RouteViewModel(mContext, route));
    }

    @Override
    protected void onBindItem(ViewDataBinding binding, User user) {
        ((RowLayoutUserBinding) binding).setUser(new UserItemViewModel(mContext, user));
    }
}