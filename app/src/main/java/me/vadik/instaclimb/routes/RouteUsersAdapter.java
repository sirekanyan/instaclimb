package me.vadik.instaclimb.routes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.vadik.instaclimb.common.RecyclerViewAdapter;
import me.vadik.instaclimb.databinding.RouteCardBinding;
import me.vadik.instaclimb.databinding.RowlayoutuserBinding;
import me.vadik.instaclimb.model.Route;
import me.vadik.instaclimb.model.User;
import me.vadik.instaclimb.viewmodel.UserViewModel;

/**
 * User: vadik
 * Date: 4/13/16
 */
public class RouteUsersAdapter extends RecyclerViewAdapter {
    private final Context context;
    private Route route;

    public RouteUsersAdapter(Context context) {
        super(new ArrayList<User>());
        this.context = context;
    }

    public void setRoute(Route route) {
        this.route = route;
        route.setClimbedCount(mDataset.size()); // TODO: not updated every time
        this.notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdapter.BindingHolder bindingHolder, int position) {

        if (position == 0) {
            if (route != null) {
                RouteCardBinding routeCardBinding = (RouteCardBinding) bindingHolder.getBinding();
                routeCardBinding.setRoute(route);
                routeCardBinding.setFormatter(new RouteTextFormatter(context, route));
//                routeCard2Binding.executePendingBindings();
            }
            return;
        }

        final User user = (User) mDataset.get(position - 1);

        RowlayoutuserBinding rowUserBinding = (RowlayoutuserBinding) bindingHolder.getBinding();
        rowUserBinding.setUser(user);
        rowUserBinding.setHandler(new UserViewModel(user));
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 1;
        }
        return 0;
    }

    @Override
    public RecyclerViewAdapter.BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case 1:
                RouteCardBinding binding = RouteCardBinding.inflate(inflater, parent, false);
                return new BindingHolder<>(binding.getRoot());
            default:
                RowlayoutuserBinding rowUserBinding = RowlayoutuserBinding.inflate(inflater, parent, false);
                return new BindingHolder<>(rowUserBinding.getRoot());
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }
}