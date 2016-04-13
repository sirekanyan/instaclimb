package me.vadik.instaclimb.routes;

import android.content.res.Resources;
import android.support.v4.widget.SimpleCursorAdapter;

import me.vadik.instaclimb.R;

/**
 * User: vadik
 * Date: 4/13/16
 */
public class RouteHelper {
    public static String getName(Resources res, String routeName) {
        if (routeName.matches(".?\\d*?")) {
            routeName = res.getString(R.string.route) + " " + routeName;
        }
        return routeName;
    }
}
