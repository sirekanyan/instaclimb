package me.vadik.instaclimb.android;

import android.database.Cursor;

/**
 * User: vadik
 * Date: 4/13/16
 */
public class CursorHelper {

    private final Cursor cursor;

    public CursorHelper(Cursor cursor) {
        this.cursor = cursor;
    }

    public String getString(String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public int getInt(String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public boolean getBoolean(String columnName) {
        return 1 == getInt(columnName);
    }
}
