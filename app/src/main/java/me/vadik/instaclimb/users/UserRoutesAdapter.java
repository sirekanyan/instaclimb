package me.vadik.instaclimb.users;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.vadik.instaclimb.common.RecyclerViewAdapter;
import me.vadik.instaclimb.databinding.RowlayoutrouteBinding;
import me.vadik.instaclimb.model.Route;
import me.vadik.instaclimb.viewmodel.RouteViewModel;

/**
 * User: vadik
 * Date: 4/13/16
 */
public class UserRoutesAdapter extends RecyclerViewAdapter {
    public UserRoutesAdapter() {
        super(new ArrayList<Route>());
    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdapter.BindingHolder bindingHolder, int position) {
        final Route route = (Route) mDataset.get(position);

        RowlayoutrouteBinding rowRouteBinding = (RowlayoutrouteBinding) bindingHolder.getBinding();
        rowRouteBinding.setRoute(route);
        rowRouteBinding.setHandler(new RouteViewModel(route));
    }

    @Override
    public RecyclerViewAdapter.BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RowlayoutrouteBinding rowRouteBinding = RowlayoutrouteBinding.inflate(inflater, parent, false);
        return new BindingHolder<>(rowRouteBinding.getRoot());
    }
}