package me.vadik.instaclimb.provider;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import me.vadik.instaclimb.helper.RouteHelper;
import me.vadik.instaclimb.model.contract.RouteContract;

/**
 * User: vadik
 * Date: 4/14/16
 */
public class RouteProvider {
    public static String getRouteName(Context context, String routeId) {
        String routeName = null;
        Cursor cursor = context.getContentResolver().query(
                Uri.withAppendedPath(RoutesContentProvider.CONTENT_URI, "routes"),
                new String[]{RouteContract.NAME},
                RouteContract._ID + " = ?",
                new String[]{routeId},
                null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                routeName = cursor.getString(cursor.getColumnIndex(RouteContract.NAME));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        if (routeName != null) {
            routeName = RouteHelper.getName(context, routeName);
        }
        return routeName;
    }
}
