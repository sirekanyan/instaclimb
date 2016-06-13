package me.vadik.instaclimb.provider;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import me.vadik.instaclimb.model.Route;
import me.vadik.instaclimb.model.User;
import me.vadik.instaclimb.model.common.CommonObject;

/**
 * User: vadik
 * Date: 6/13/16
 */
public class ProviderHelper {

    public static Route getRoute(Context context, int routeId) {
        return getObject(context, "routes_view", routeId, new Function<Route>() {
            @Override
            public Route build(Cursor cursor) {
                return new Route.Builder(cursor).build();
            }

            @Override
            public Route onError(int id) {
                return new Route.Builder(id, "noname").build();
            }
        });
    }

    public static User getUser(Context context, int userId) {
        return getObject(context, "users", userId, new Function<User>() {
            @Override
            public User build(Cursor cursor) {
                return new User.Builder(cursor).build();
            }

            @Override
            public User onError(int id) {
                return new User.Builder(id, "noname").build();
            }
        });
    }

    private static <T extends CommonObject> T getObject(Context context, String table, int id, Function<T> builder) {
        Uri uri = Uri.withAppendedPath(RoutesContentProvider.CONTENT_URI, table);
        Uri singleUri = ContentUris.withAppendedId(uri, id);
        Cursor cursor = context.getContentResolver().query(singleUri, null, null, null, null);
        T object = null;
        try {
            if (cursor != null && cursor.moveToFirst()) {
                object = builder.build(cursor);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        if (object == null) {
            object = builder.onError(id);
        }
        return object;
    }

    private interface Function<H> {
        H build(Cursor cursor);
        H onError(int id);
    }
}
