package me.vadik.instaclimb.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import me.vadik.instaclimb.model.Marker;
import me.vadik.instaclimb.model.Route;
import me.vadik.instaclimb.view.RouteActivity;
import me.vadik.instaclimb.viewmodel.common.CommonViewModel;

/**
 * User: vadik
 * Date: 5/8/16
 */
public class RouteItemViewModel extends CommonViewModel<Route> {

    private final Route route;
    private final Marker marker;

    public RouteItemViewModel(Context context, Route route) {
        super(context, route);
        this.route = route;
        this.marker = new Marker(route.color1, route.color2, route.color3);
    }

    public void openRoute(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, RouteActivity.class);
        intent.putExtra(RouteActivity.ARG_ROUTE_ID, route.id);
        intent.putExtra(RouteActivity.ARG_ROUTE_NAME, route.name);
        context.startActivity(intent);
    }

    public Marker getMarker() {
        return marker;
    }

    public String getDate() {
        return route.createdWhen;
    }

    public boolean isFlash() {
        return route.done == 2;
    }

    public String getGrade() {
        return route.grade;
    }
}
