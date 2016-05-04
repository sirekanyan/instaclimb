package me.vadik.instaclimb.routes;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;

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

    @Deprecated
    public static void setMarkerColor(View view, int color) {
        setMarkerColor(view, color, 0);
    }

    @Deprecated
    public static void setMarkerColor(View view, int color, int defaultResId) {
        TypedArray colors = view.getContext().getResources().obtainTypedArray(R.array.colors);
        if (color == 0) {
            if (defaultResId == 0) {
                view.setVisibility(View.GONE);
            } else {
                view.setBackgroundResource(defaultResId);
                view.setVisibility(View.VISIBLE);
            }
        } else {
            view.setBackgroundResource(colors.getResourceId(color, 0));
            view.setVisibility(View.VISIBLE);
        }
        colors.recycle();
    }
}
