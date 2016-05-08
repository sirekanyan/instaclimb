package me.vadik.instaclimb.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import me.vadik.instaclimb.databinding.RowLayoutRouteBinding;
import me.vadik.instaclimb.model.Route;
import me.vadik.instaclimb.viewmodel.RouteItemViewModel;

/**
 * User: vadik
 * Date: 4/13/16
 */
public class UserRoutesAdapter extends RecyclerViewAdapter<Route> {
    public UserRoutesAdapter(Context context) {
        super(context);
    }

    @Override
    protected void onBindRowViewHolder(BindingHolder bindingHolder, Route route) {
        RowLayoutRouteBinding rowRouteBinding = (RowLayoutRouteBinding) bindingHolder.getBinding();
        rowRouteBinding.setRoute(new RouteItemViewModel(context, route));
        rowRouteBinding.executePendingBindings();
    }

    @Override
    protected BindingHolder onCreateRowViewHolder(LayoutInflater inflater, ViewGroup parent) {
        RowLayoutRouteBinding rowRouteBinding = RowLayoutRouteBinding.inflate(inflater, parent, false);
        return new BindingHolder<>(rowRouteBinding.getRoot());
    }
}