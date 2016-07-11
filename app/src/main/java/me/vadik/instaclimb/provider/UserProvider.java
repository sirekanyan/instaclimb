package me.vadik.instaclimb.provider;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import me.vadik.instaclimb.model.contract.UserContract;

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

    public static String getUserName(Context context, int userId) {
        return getUserName(context, String.valueOf(userId));
    }

    public static boolean hasPicture(Context context, int userId) {
        boolean hasPicture = false;
        Cursor cursor = context.getContentResolver().query(
                Uri.withAppendedPath(RoutesContentProvider.CONTENT_URI, "users"),
                new String[]{UserContract.HAS_PICTURE},
                UserContract._ID + " = ?",
                new String[]{String.valueOf(userId)},
                null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                hasPicture = 1 == cursor.getInt(cursor.getColumnIndex(UserContract.HAS_PICTURE));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return hasPicture;
    }
}
