package me.vadik.instaclimb.routes;

import android.content.Context;

import me.vadik.instaclimb.R;

/**
 * User: vadik
 * Date: 4/13/16
 */
public class RouteHelper {
    public static String getName(Context context, String routeName) {
        if (routeName.matches(".?\\d*?")) {
            routeName = context.getResources().getString(R.string.route) + " " + routeName;
        }
        return routeName;
    }
}
