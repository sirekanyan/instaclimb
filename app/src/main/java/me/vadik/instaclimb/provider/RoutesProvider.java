package me.vadik.instaclimb.provider;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

/**
 * User: vadik
 * Date: 4/14/16
 */
public class RoutesProvider {
    public static int prepareRoutesForUser(Context context, String userId) {
        clearClimbedRoutes(context);
        Uri uri = Uri.withAppendedPath(RoutesContentProvider.CONTENT_URI, "routes");
        ContentValues values = new ContentValues();
        values.put("done", 1);
        String select = "_id in (select route_id from users_routes where user_id = ?)";
        String[] args = new String[]{userId};
        return context.getContentResolver().update(
                uri,
                values,
                select,
                args);
    }

    private static int clearClimbedRoutes(Context context) {
        Uri uri = Uri.withAppendedPath(RoutesContentProvider.CONTENT_URI, "routes");
        ContentValues values = new ContentValues();
        values.put("done", 0);
        return context.getContentResolver().update(uri, values, null, null);
    }
}
