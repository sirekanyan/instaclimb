package me.vadik.instaclimb.provider;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import me.vadik.instaclimb.contract.UserContract;

/**
 * User: vadik
 * Date: 4/14/16
 */
public class UserProvider {
    public static String getUserName(Context context, String userId) {
        String userName = null;
        Cursor cursor = context.getContentResolver().query(
                Uri.withAppendedPath(RoutesContentProvider.CONTENT_URI, "users"),
                new String[]{UserContract.NAME},
                UserContract._ID + " = ?",
                new String[]{userId},
                null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                userName = cursor.getString(cursor.getColumnIndex(UserContract.NAME));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return userName;
    }
}
