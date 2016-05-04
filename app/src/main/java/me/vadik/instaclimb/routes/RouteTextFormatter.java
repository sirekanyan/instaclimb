package me.vadik.instaclimb.routes;

import android.content.Context;
import android.graphics.drawable.Drawable;

import me.vadik.instaclimb.R;
import me.vadik.instaclimb.model.Route;

/**
 * User: vadik
 * Date: 5/4/16
 */
public class RouteTextFormatter {
    private final Context context;
    private final Route route;

    public RouteTextFormatter(Context context, Route route) {
        this.context = context;
        this.route = route;
    }

    public String getClimbedCount() {
        int resId = R.plurals.number_of_users_who_climbed;
        int count = route.getClimbedCount();
        return context.getResources().getQuantityString(resId, count, count);
    }
}
