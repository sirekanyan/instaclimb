package me.vadik.instaclimb.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import me.vadik.instaclimb.model.Route;
import me.vadik.instaclimb.routes.RouteActivity;
import me.vadik.instaclimb.users.UserActivity;

/**
 * User: vadik
 * Date: 5/3/16
 */
public class RouteViewModel {
    private final Route route;

    public RouteViewModel(Route route) {
        this.route = route;
    }

    public void openRoute(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, RouteActivity.class);
        intent.putExtra(RouteActivity.ARG_ROUTE_ID, route.getId());
        intent.putExtra(RouteActivity.ARG_ROUTE_NAME, route.getName());
        context.startActivity(intent);
    }
}
